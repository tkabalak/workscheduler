package com.chmura.exam.workingscheduler.repository;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.chmura.exam.workingscheduler.entity.Task;
import com.chmura.exam.workingscheduler.entity.Worker;
import com.chmura.exam.workingscheduler.repository.exceptions.IllegalOrphanException;
import com.chmura.exam.workingscheduler.repository.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class WorkerJpaController implements Serializable {

    public WorkerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Worker worker) {
        if (worker.getTaskCollection() == null) {
            worker.setTaskCollection(new ArrayList<Task>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Task> attachedTaskCollection = new ArrayList<Task>();
            for (Task taskCollectionTaskToAttach : worker.getTaskCollection()) {
                taskCollectionTaskToAttach = em.getReference(taskCollectionTaskToAttach.getClass(), taskCollectionTaskToAttach.getIdTask());
                attachedTaskCollection.add(taskCollectionTaskToAttach);
            }
            worker.setTaskCollection(attachedTaskCollection);
            em.persist(worker);
            for (Task taskCollectionTask : worker.getTaskCollection()) {
                Worker oldWorkerIdOfTaskCollectionTask = taskCollectionTask.getWorkerId();
                taskCollectionTask.setWorkerId(worker);
                taskCollectionTask = em.merge(taskCollectionTask);
                if (oldWorkerIdOfTaskCollectionTask != null) {
                    oldWorkerIdOfTaskCollectionTask.getTaskCollection().remove(taskCollectionTask);
                    oldWorkerIdOfTaskCollectionTask = em.merge(oldWorkerIdOfTaskCollectionTask);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Worker worker) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Worker persistentWorker = em.find(Worker.class, worker.getId());
            Collection<Task> taskCollectionOld = persistentWorker.getTaskCollection();
            Collection<Task> taskCollectionNew = worker.getTaskCollection();
            List<String> illegalOrphanMessages = null;
            for (Task taskCollectionOldTask : taskCollectionOld) {
                if (!taskCollectionNew.contains(taskCollectionOldTask)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Task " + taskCollectionOldTask + " since its workerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Task> attachedTaskCollectionNew = new ArrayList<Task>();
            for (Task taskCollectionNewTaskToAttach : taskCollectionNew) {
                taskCollectionNewTaskToAttach = em.getReference(taskCollectionNewTaskToAttach.getClass(), taskCollectionNewTaskToAttach.getIdTask());
                attachedTaskCollectionNew.add(taskCollectionNewTaskToAttach);
            }
            taskCollectionNew = attachedTaskCollectionNew;
            worker.setTaskCollection(taskCollectionNew);
            worker = em.merge(worker);
            for (Task taskCollectionNewTask : taskCollectionNew) {
                if (!taskCollectionOld.contains(taskCollectionNewTask)) {
                    Worker oldWorkerIdOfTaskCollectionNewTask = taskCollectionNewTask.getWorkerId();
                    taskCollectionNewTask.setWorkerId(worker);
                    taskCollectionNewTask = em.merge(taskCollectionNewTask);
                    if (oldWorkerIdOfTaskCollectionNewTask != null && !oldWorkerIdOfTaskCollectionNewTask.equals(worker)) {
                        oldWorkerIdOfTaskCollectionNewTask.getTaskCollection().remove(taskCollectionNewTask);
                        oldWorkerIdOfTaskCollectionNewTask = em.merge(oldWorkerIdOfTaskCollectionNewTask);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = worker.getId();
                if (findWorker(id) == null) {
                    throw new NonexistentEntityException("The worker with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Worker worker;
            try {
                worker = em.getReference(Worker.class, id);
                worker.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The worker with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Task> taskCollectionOrphanCheck = worker.getTaskCollection();
            for (Task taskCollectionOrphanCheckTask : taskCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Worker (" + worker + ") cannot be destroyed since the Task " + taskCollectionOrphanCheckTask + " in its taskCollection field has a non-nullable workerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(worker);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Worker> findWorkerEntities() {
        return findWorkerEntities(true, -1, -1);
    }

    public List<Worker> findWorkerEntities(int maxResults, int firstResult) {
        return findWorkerEntities(false, maxResults, firstResult);
    }

    private List<Worker> findWorkerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Worker.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Worker findWorker(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Worker.class, id);
        } finally {
            em.close();
        }
    }

    public int getWorkerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Worker> rt = cq.from(Worker.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
