package br.com.zone.tacocloud.repository;

import br.com.zone.tacocloud.model.Order;

public interface OrderRepository {

    Order save(Order order);

}
