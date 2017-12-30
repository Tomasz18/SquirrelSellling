package spoonarchsystems.squirrelselling.Model.DAO;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spoonarchsystems.squirrelselling.Model.Entity.Ware;

import javax.transaction.Transactional;

@Transactional
@Repository
public class WareDAOImpl implements WareDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Ware getWare(int id) {
        return sessionFactory.getCurrentSession().get(Ware.class, new Integer(id));
    }
}
