package core.basesyntax.dao.ma;

import core.basesyntax.exceptions.DataProcessingException;
import core.basesyntax.model.ma.Coach;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class CoachDaoImpl extends PersonDaoImpl implements CoachDao {
    public CoachDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Coach> findByExperienceGreaterThan(int years) {
        try (Session session = sessionFactory.openSession()) {
            Query<Coach> getCoachesByExperienceGreaterThan = session
                    .createQuery("from Coach c where c.experience > :years", Coach.class);
            getCoachesByExperienceGreaterThan.setParameter("years", years);

            return getCoachesByExperienceGreaterThan.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find coaches with experience greater than "
                    + years, e);
        }
    }
}
