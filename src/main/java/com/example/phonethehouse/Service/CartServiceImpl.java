package com.example.phonethehouse.Service;

import com.example.phonethehouse.Model.CartItem;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartServiceImpl implements CartService {

    private Map<Integer, CartItem> map = new LinkedHashMap<>();

    @Override
    public void add(CartItem item) {
        CartItem existing = map.get(item.getPhoneId());
        if (existing == null) {
            map.put(item.getPhoneId(), item);
        } else {
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
        }
    }

    @Override
    public void update(Integer phoneId, Integer quantity) {
        CartItem item = map.get(phoneId);
        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    @Override
    public void remove(Integer phoneId) {
        map.remove(phoneId);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Collection<CartItem> getItems() {
        return map.values();
    }

    @Override
    public int getCount() {
        return map.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Override
    public double getAmount() {
        return map.values().stream().mapToDouble(CartItem::getTotal).sum();
    }
}
