package spoonarchsystems.squirrelselling.Model.DAO;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spoonarchsystems.squirrelselling.Model.Entity.Ware;

import javax.transaction.Transactional;

/**
 * Data Access Object provides database operations on {@link Ware} objects
 */
@Transactional
@Repository
public class WareDAOImpl implements WareDAO {
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Fetches and returns {@link Ware} with given id
     * @param id ware id to fetch
     * @return ware with given id
     */
    @Override
    public Ware getWare(int id) {
        return sessionFactory.getCurrentSession().get(Ware.class, new Integer(id));
    }
}
