package core.basesyntax.dao.machine;

import core.basesyntax.dao.AbstractDao;
import core.basesyntax.exceptions.DataProcessingException;
import core.basesyntax.model.machine.Machine;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class MachineDaoImpl extends AbstractDao implements MachineDao {
    public MachineDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Machine save(Machine machine) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            session.persist(machine);
            transaction.commit();

            return machine;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert machine "
                    + machine + " to database", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Machine> findByAgeOlderThan(int age) {
        int thisYear = LocalDate.now().getYear();

        try (Session session = sessionFactory.openSession()) {
            Query<Machine> getMachinesByAgeOlderThan = session
                    .createQuery("from Machine m where (:this_year - m.year) > :age",
                            Machine.class);
            getMachinesByAgeOlderThan.setParameter("this_year", thisYear);
            getMachinesByAgeOlderThan.setParameter("age", age);

            return getMachinesByAgeOlderThan.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get machines with age older than " + age, e);
        }
    }
}
