// package jpa2.sample;
//
// import junit.framework.TestCase;
//
// import java.util.List;
// import javax.persistence.EntityManager;
// import javax.persistence.EntityManagerFactory;
// import javax.persistence.Persistence;
//
//
// /**
//  * Unit test for simple App.
//  */
// public class JPATest extends TestCase {
//
//   public void testApp() {
//
//     /* Create EntityManagerFactory */
//     EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample");
//
//     /* Create and populate Entity */
//     Employee employee = new Employee();
//     employee.setFirstname("prasaddasdassad");
//     employee.setLastname("kharsdasdsakar");
//     employee.setEmail("someMaisadasdsal@gmail.com");
//
//     /* Create EntityManager */
//     EntityManager em = emf.createEntityManager();
//
//     /* Persist entity */
//     em.getTransaction().begin();
//     em.persist(employee);
//     em.getTransaction().commit();
//
//     /* Retrieve entity */
//     employee = em.find(Employee.class, 3);
//     System.out.println(employee);
//
//     /* Update entity */
//     em.getTransaction().begin();
//     employee.setFirstname("Prasdsdnil");
//     System.out.println("Employee after updation :- " + employee);
//     em.getTransaction().commit();
//
//     /* Remove entity */
//     em.getTransaction().begin();
//     em.remove(employee);
//     em.getTransaction().commit();
//
//     /* Check whether enittiy is removed or not */
//     employee = em.find(Employee.class, 1);
//     System.out.println("Employee after removal :- " + employee);
//
//     // Test
//     List<Employee> employees = em.createNamedQuery("Hi").getResultList();
//     for (Employee someEmployee : employees) {
//       System.out.println(someEmployee);
//     }
//
//   }
// }
