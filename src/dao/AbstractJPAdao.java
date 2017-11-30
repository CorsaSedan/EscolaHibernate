/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import model.iEntity;

/**
 *
 * @author Aluno
 * @param <T>
 */
public abstract class AbstractJPAdao<T extends iEntity> implements iDao<T> {

    private final Class<T> clazz;

    public AbstractJPAdao() {
        clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void save(T t) {
        JpaUtil.getEntityManager().getTransaction().begin();
        if (t.getId() == 0) {
            JpaUtil.getEntityManager().persist(t);
        } else {
            JpaUtil.getEntityManager().merge(t);
        }
        JpaUtil.getEntityManager().getTransaction().commit();
    }

    @Override
    public T findById(int id) {
        return JpaUtil.getEntityManager().find(clazz, id);
    }

    @Override
    @SuppressWarnings("JPQLValidation")
    public List<T> findAll() {
        return JpaUtil.getEntityManager()
                .createQuery("select c from " + clazz.getSimpleName() + " c")
                .getResultList();
    }

    public void delete(int id) {
        JpaUtil.getEntityManager().remove(
                JpaUtil.getEntityManager()
                .getReference(clazz, id));
    }

    @Override
    public void delete(T t) {
        delete(t.getId());
    }

}
