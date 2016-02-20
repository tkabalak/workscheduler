package com.chmura.exam.workingscheduler.repository;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.chmura.exam.workingscheduler.entity.Worker;
import com.chmura.exam.workingscheduler.entity.ResultsEntity;
import com.chmura.exam.workingscheduler.entity.Task;
import com.chmura.exam.workingscheduler.repository.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class TaskJpaController implements Serializable {

    public TaskJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Task task) {
        if (task.getResultsEntityCollection() == null) {
            task.setResultsEntityCollection(new ArrayList<ResultsEntity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Worker workerId = task.getWorkerId();
            if (workerId != null) {
                workerId = em.getReference(workerId.getClass(), workerId.getId());
                task.setWorkerId(workerId);
            }
            Collection<ResultsEntity> attachedResultsEntityCollection = new ArrayList<ResultsEntity>();
            for (ResultsEntity resultsEntityCollectionResultsEntityToAttach : task.getResultsEntityCollection()) {
                resultsEntityCollectionResultsEntityToAttach = em.getReference(resultsEntityCollectionResultsEntityToAttach.getClass(), resultsEntityCollectionResultsEntityToAttach.getIdRealzation());
                attachedResultsEntityCollection.add(resultsEntityCollectionResultsEntityToAttach);
            }
            task.setResultsEntityCollection(attachedResultsEntityCollection);
            em.persist(task);
            if (workerId != null) {
                workerId.getTaskCollection().add(task);
                workerId = em.merge(workerId);
            }
            for (ResultsEntity resultsEntityCollectionResultsEntity : task.getResultsEntityCollection()) {
                Task oldTaskIdOfResultsEntityCollectionResultsEntity = resultsEntityCollectionResultsEntity.getTaskId();
                resultsEntityCollectionResultsEntity.setTaskId(task);
                resultsEntityCollectionResultsEntity = em.merge(resultsEntityCollectionResultsEntity);
                if (oldTaskIdOfResultsEntityCollectionResultsEntity != null) {
                    oldTaskIdOfResultsEntityCollectionResultsEntity.getResultsEntityCollection().remove(resultsEntityCollectionResultsEntity);
                    oldTaskIdOfResultsEntityCollectionResultsEntity = em.merge(oldTaskIdOfResultsEntityCollectionResultsEntity);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Task task) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Task persistentTask = em.find(Task.class, task.getIdTask());
            Worker workerIdOld = persistentTask.getWorkerId();
            Worker workerIdNew = task.getWorkerId();
            Collection<ResultsEntity> resultsEntityCollectionOld = persistentTask.getResultsEntityCollection();
            Collection<ResultsEntity> resultsEntityCollectionNew = task.getResultsEntityCollection();
            if (workerIdNew != null) {
                workerIdNew = em.getReference(workerIdNew.getClass(), workerIdNew.getId());
                task.setWorkerId(workerIdNew);
            }
            Collection<ResultsEntity> attachedResultsEntityCollectionNew = new ArrayList<ResultsEntity>();
            for (ResultsEntity resultsEntityCollectionNewResultsEntityToAttach : resultsEntityCollectionNew) {
                resultsEntityCollectionNewResultsEntityToAttach = em.getReference(resultsEntityCollectionNewResultsEntityToAttach.getClass(), resultsEntityCollectionNewResultsEntityToAttach.getIdRealzation());
                attachedResultsEntityCollectionNew.add(resultsEntityCollectionNewResultsEntityToAttach);
            }
            resultsEntityCollectionNew = attachedResultsEntityCollectionNew;
            task.setResultsEntityCollection(resultsEntityCollectionNew);
            task = em.merge(task);
            if (workerIdOld != null && !workerIdOld.equals(workerIdNew)) {
                workerIdOld.getTaskCollection().remove(task);
                workerIdOld = em.merge(workerIdOld);
            }
            if (workerIdNew != null && !workerIdNew.equals(workerIdOld)) {
                workerIdNew.getTaskCollection().add(task);
                workerIdNew = em.merge(workerIdNew);
            }
            for (ResultsEntity resultsEntityCollectionOldResultsEntity : resultsEntityCollectionOld) {
                if (!resultsEntityCollectionNew.contains(resultsEntityCollectionOldResultsEntity)) {
                    resultsEntityCollectionOldResultsEntity.setTaskId(null);
                    resultsEntityCollectionOldResultsEntity = em.merge(resultsEntityCollectionOldResultsEntity);
                }
            }
            for (ResultsEntity resultsEntityCollectionNewResultsEntity : resultsEntityCollectionNew) {
                if (!resultsEntityCollectionOld.contains(resultsEntityCollectionNewResultsEntity)) {
                    Task oldTaskIdOfResultsEntityCollectionNewResultsEntity = resultsEntityCollectionNewResultsEntity.getTaskId();
                    resultsEntityCollectionNewResultsEntity.setTaskId(task);
                    resultsEntityCollectionNewResultsEntity = em.merge(resultsEntityCollectionNewResultsEntity);
                    if (oldTaskIdOfResultsEntityCollectionNewResultsEntity != null && !oldTaskIdOfResultsEntityCollectionNewResultsEntity.equals(task)) {
                        oldTaskIdOfResultsEntityCollectionNewResultsEntity.getResultsEntityCollection().remove(resultsEntityCollectionNewResultsEntity);
                        oldTaskIdOfResultsEntityCollectionNewResultsEntity = em.merge(oldTaskIdOfResultsEntityCollectionNewResultsEntity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = task.getIdTask();
                if (findTask(id) == null) {
                    throw new NonexistentEntityException("The task with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Task task;
            try {
                task = em.getReference(Task.class, id);
                task.getIdTask();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The task with id " + id + " no longer exists.", enfe);
            }
            Worker workerId = task.getWorkerId();
            if (workerId != null) {
                workerId.getTaskCollection().remove(task);
                workerId = em.merge(workerId);
            }
            Collection<ResultsEntity> resultsEntityCollection = task.getResultsEntityCollection();
            for (ResultsEntity resultsEntityCollectionResultsEntity : resultsEntityCollection) {
                resultsEntityCollectionResultsEntity.setTaskId(null);
                resultsEntityCollectionResultsEntity = em.merge(resultsEntityCollectionResultsEntity);
            }
            em.remove(task);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Task> findTaskEntities() {
        return findTaskEntities(true, -1, -1);
    }

    public List<Task> findTaskEntities(int maxResults, int firstResult) {
        return findTaskEntities(false, maxResults, firstResult);
    }

    private List<Task> findTaskEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Task.class));
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

    public Task findTask(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Task.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaskCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Task> rt = cq.from(Task.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
