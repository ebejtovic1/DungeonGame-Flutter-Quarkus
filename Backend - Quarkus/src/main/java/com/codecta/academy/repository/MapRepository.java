package com.codecta.academy.repository;

import com.codecta.academy.repository.entity.Item;
import com.codecta.academy.repository.entity.Map;
import com.codecta.academy.repository.entity.Monster;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class MapRepository extends Repository<Map, Integer> {
    public MapRepository() {
        super(Map.class);
    }
}
