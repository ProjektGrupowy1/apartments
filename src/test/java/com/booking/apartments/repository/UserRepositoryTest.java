package com.booking.apartments.repository;

import com.booking.apartments.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUserByEmailTest() {

        //given
        UserEntity user = new UserEntity("Jan", "Kowalski", "jan.kowalski@wp.pl",
                "jan", "+48503654234", "ul. Waryńskiego 34", 1, 1, 1);
        entityManager.persist(user);
        entityManager.flush();

        // when
        UserEntity found = userRepository.findUserByEmail(user.getEmail()).get(0);

        // then
        assertThat(found.getIdUser()).isEqualTo(user.getIdUser());
        assertThat(found.getName()).isEqualTo(user.getName());
        assertThat(found.getLastname()).isEqualTo(user.getLastname());
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
        assertThat(found.getPassword()).isEqualTo(user.getPassword());
        assertThat(found.getPhone()).isEqualTo(user.getPhone());
        assertThat(found.getStreet()).isEqualTo(user.getStreet());
        assertThat(found.getIdProfile()).isEqualTo(user.getIdProfile());
        assertThat(found.getIdCity()).isEqualTo(user.getIdCity());

    }

    @Test
    public void findUserByIdTest() {

        //given
        UserEntity user = new UserEntity("Jan", "Kowalski", "jan.kowalski@wp.pl",
                "jan", "+48503654234", "ul. Waryńskiego 34", 1, 1, 1);
        entityManager.persist(user);
        entityManager.flush();

        // when
        UserEntity found = userRepository.findUserById(user.getIdUser()).get(0);

        // then
        assertThat(found.getIdUser()).isEqualTo(user.getIdUser());
        assertThat(found.getName()).isEqualTo(user.getName());
        assertThat(found.getLastname()).isEqualTo(user.getLastname());
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
        assertThat(found.getPassword()).isEqualTo(user.getPassword());
        assertThat(found.getPhone()).isEqualTo(user.getPhone());
        assertThat(found.getStreet()).isEqualTo(user.getStreet());
        assertThat(found.getIdProfile()).isEqualTo(user.getIdProfile());
        assertThat(found.getIdCity()).isEqualTo(user.getIdCity());
    }

    @Test
    public void findAllUsersTest() {

        //given
        UserEntity user = new UserEntity("Jan", "Kowalski", "jan.kowalski@wp.pl",
                "jan", "+48503654234", "ul. Waryńskiego 34", 1, 1, 1);
        UserEntity user2 = new UserEntity("Zbigniew", "Nowak", "zbigniew.nowak@wp.pl",
                "zbigniew", "+48503987234", "ul. Paderewskiego", 1, 1, 1);
        UserEntity user3 = new UserEntity("Malwina", "Nowacka", "malwina.nowacka@wp.pl",
                "malwina", "+48950487235", "ul. Wojska Polskiego 98", 1, 1, 1);
        entityManager.persist(user);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        // when
        List<UserEntity> found = userRepository.findAllUsers();

        // then
        assertTrue(found.contains(user));
        assertTrue(found.contains(user2));
        assertTrue(found.contains(user3));

    }


    @Test
    public void saveUserTest() {

        //given
        UserEntity user = new UserEntity("Jan", "Kowalski", "jan.kowalski@wp.pl",
                "jan", "+48503654234", "ul. Waryńskiego 34", 1, 1, 1);

        // when
        userRepository.save(user);

        UserEntity found = userRepository.findUserById(user.getIdUser()).get(0);

        // then
        assertThat(found.getIdUser()).isEqualTo(user.getIdUser());
        assertThat(found.getName()).isEqualTo(user.getName());
        assertThat(found.getLastname()).isEqualTo(user.getLastname());
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
        assertThat(found.getPassword()).isEqualTo(user.getPassword());
        assertThat(found.getPhone()).isEqualTo(user.getPhone());
        assertThat(found.getStreet()).isEqualTo(user.getStreet());
        assertThat(found.getIdProfile()).isEqualTo(user.getIdProfile());
        assertThat(found.getIdCity()).isEqualTo(user.getIdCity());
    }

    @Test
    public void removeUserTest() {

        //given
        UserEntity user = new UserEntity("Jan", "Kowalski", "jan.kowalski@wp.pl",
                "jan", "+48503654234", "ul. Waryńskiego 34", 1, 1, 1);
        userRepository.save(user);
        UserEntity existing = userRepository.findUserById(user.getIdUser()).get(0);
        assertThat(existing.getEmail()).isEqualTo(user.getEmail());

        // when
        userRepository.delete(user);
        List<UserEntity> found = userRepository.findUserById(user.getIdUser());

        // then
        assertTrue(found.isEmpty());

    }
}
