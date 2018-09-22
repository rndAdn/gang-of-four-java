package com.radequin.gangoffour.repository;

import com.radequin.gangoffour.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findOneByUserName(String userName);

    @Override
    List<User> findAll();

    User findOneById(long id);

    User findOneByUserNameAndPassword(String username, String password);
}
