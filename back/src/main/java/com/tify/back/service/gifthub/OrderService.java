package com.tify.back.service.gifthub;


import com.tify.back.dto.admin.OrderStateDto;
import com.tify.back.dto.pay.request.RefundRequestDto;
import com.tify.back.exception.OrderAlreadyExistException;
import com.tify.back.model.gifthub.Gift;
import com.tify.back.model.gifthub.Order;
import com.tify.back.model.pay.Pay;
import com.tify.back.model.users.User;
import com.tify.back.model.wish.Wish;
import com.tify.back.repository.gifthub.OrderRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static net.minidev.json.JSONValue.isValidJson;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final GiftService giftService;

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> saveOrders(List<Order> orders) {
        return orderRepository.saveAll(orders);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
    public String deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return "order removed !!" + id;
    }
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public String deleteOrderById(Long id) {
        orderRepository.deleteById(id);
        return "order removed!!" + id;
    }

    /*
    public Order createOrder(String message) throws JSONException {
        JSONObject map;
        if (isValidJson(message)) {
            map = new JSONObject(message);
        } else {
            throw new JSONException("Invalid JSON");
        }
        Order order = new Order();
        Gift gift = giftService.getGiftById(map.getLong("giftId"));
        if (gift.getOrder() != null) {
            throw new OrderAlreadyExistException("This gift's order is already taken.");
        }
        //order.setGift(gift);
        order.setTel(map.getString("tel"));
        order.setGatheredPrice(map.getInt("gatheredPrice"));
        order.setOrderPrice(map.getInt("orderPrice"));
        order.setDeliveryNumber(map.getString("deliveryNumber"));
        order.setState(map.getInt("state"));
        try {
            order.setUser(gift.getWish().getUser());
        } catch (Exception e) {
            order.setUser(null);
        }
        orderRepository.save(order);
        gift.setOrder(order);
        giftService.saveGift(gift);

        return order;
    }
     */

    /**
     * ???????????????, ???????????? ?????? - ?????????
     */
    public Order updateDeliverState(OrderStateDto dto) {
        Order order = orderRepository.getReferenceById(dto.getOrderId());
        order.setDeliveryNumber(dto.getDeliveryNumber());
        order.setState(dto.getState());
        return order;
    }

    /**
     * ????????????
     */
    public Order addNewOrder(Gift gift, User user) {
        Wish wish = gift.getWish();
        System.out.println("?????? gift??? wish:"+wish);

        String year = Integer.toString(LocalDateTime.now().getYear());
        String month = Integer.toString(LocalDateTime.now().getMonthValue());
        String day = Integer.toString(LocalDateTime.now().getDayOfMonth());

        //?????? ??????
        Order order = orderRepository.save(
                Order.builder()
                        .gift_gift_id(gift.getId())
                        .gift_product_id(gift.getProductId())
                        .userOption(gift.getUserOption())
                        .giftImgUrl(gift.getGiftImgUrl())
                        .giftName(gift.getGiftname())
                        .wishId(wish.getId())
                        .wishName(wish.getTitle())
                        .orderPrice(gift.getMaxAmount())
                        .tel(user.getTel())
                        .gatheredPrice(gift.getGathered())
                        .user(user)
                        .gift(gift)
                        .wishFinishDate( wish.getEndDate().toString().substring(0,10) )
                        .deliveryNumber(null)
                        .createdTime(LocalDateTime.now())
                        .createdDt(year+"."+month+"."+day)
                        .state(0)
                        .build()
        );
        return order;
    }

    /**
     * ????????????
     */
    @Transactional
    public Order refund(RefundRequestDto refundDto) {
        Order refOrder = orderRepository.findById(refundDto.getOrderId()).get();
        refOrder.setRefInfo(refundDto.getAccount(), refundDto.getBank(), refundDto.getUserName());

        System.out.println("?????? ?????????????????????.");
        return refOrder;
    }
}
