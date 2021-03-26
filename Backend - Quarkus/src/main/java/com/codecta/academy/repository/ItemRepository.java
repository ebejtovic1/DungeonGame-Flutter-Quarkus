package com.codecta.academy.repository;

import com.codecta.academy.repository.entity.Item;


import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.MANDATORY)
public class ItemRepository extends Repository<Item, Integer>{
    public ItemRepository() {
        super(Item.class);
    }
}
