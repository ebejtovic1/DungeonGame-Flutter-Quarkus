package com.codecta.academy.repository;

import com.codecta.academy.repository.entity.Dungeon;
import com.codecta.academy.repository.entity.Monster;
import com.codecta.academy.services.model.DungeonDto;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class MonsterRepository extends Repository<Monster, Integer>{
    public MonsterRepository() {
        super(Monster.class);
    }

    @Inject
    EntityManager entityManager;

}
