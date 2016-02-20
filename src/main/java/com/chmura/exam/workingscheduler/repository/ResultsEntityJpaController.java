package com.chmura.exam.workingscheduler.repository;

import com.chmura.exam.workingscheduler.entity.ResultsEntity;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.chmura.exam.workingscheduler.entity.Task;
import com.chmura.exam.workingscheduler.repository.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class ResultsEntityJpaController implements Serializable {

    public ResultsEntityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ResultsEntity resultsEntity) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Task taskId = resultsEntity.getTaskId();
            if (taskId != null) {
                taskId = em.getReference(taskId.getClass(), taskId.getIdTask());
                resultsEntity.setTaskId(taskId);
            }
            em.persist(resultsEntity);
            if (taskId != null) {
                taskId.getResultsEntityCollection().add(resultsEntity);
                taskId = em.merge(taskId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ResultsEntity resultsEntity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ResultsEntity persistentResultsEntity = em.find(ResultsEntity.class, resultsEntity.getIdRealzation());
            Task taskIdOld = persistentResultsEntity.getTaskId();
            Task taskIdNew = resultsEntity.getTaskId();
            if (taskIdNew != null) {
                taskIdNew = em.getReference(taskIdNew.getClass(), taskIdNew.getIdTask());
                resultsEntity.setTaskId(taskIdNew);
            }
            resultsEntity = em.merge(resultsEntity);
            if (taskIdOld != null && !taskIdOld.equals(taskIdNew)) {
                taskIdOld.getResultsEntityCollection().remove(resultsEntity);
                taskIdOld = em.merge(taskIdOld);
            }
            if (taskIdNew != null && !taskIdNew.equals(taskIdOld)) {
                taskIdNew.getResultsEntityCollection().add(resultsEntity);
                taskIdNew = em.merge(taskIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = resultsEntity.getIdRealzation();
                if (findResultsEntity(id) == null) {
                    throw new NonexistentEntityException("The resultsEntity with id " + id + " no longer exists.");
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
            ResultsEntity resultsEntity;
            try {
                resultsEntity = em.getReference(ResultsEntity.class, id);
                resultsEntity.getIdRealzation();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The resultsEntity with id " + id + " no longer exists.", enfe);
            }
            Task taskId = resultsEntity.getTaskId();
            if (taskId != null) {
                taskId.getResultsEntityCollection().remove(resultsEntity);
                taskId = em.merge(taskId);
            }
            em.remove(resultsEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ResultsEntity> findResultsEntityEntities() {
        return findResultsEntityEntities(true, -1, -1);
    }

    public List<ResultsEntity> findResultsEntityEntities(int maxResults, int firstResult) {
        return findResultsEntityEntities(false, maxResults, firstResult);
    }

    private List<ResultsEntity> findResultsEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ResultsEntity.class));
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

    public ResultsEntity findResultsEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ResultsEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getResultsEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ResultsEntity> rt = cq.from(ResultsEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
