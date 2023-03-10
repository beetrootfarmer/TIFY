package com.tify.back.controller.admin;

import com.tify.back.dto.admin.EditWishDto;
import com.tify.back.dto.admin.OrderStateDto;
import com.tify.back.dto.admin.UserListMap;
import com.tify.back.dto.admin.createUserDto;
import com.tify.back.model.customerservice.FAQ;
import com.tify.back.model.customerservice.QnA;
import com.tify.back.model.gifthub.Cart;
import com.tify.back.model.gifthub.Gift;
import com.tify.back.model.gifthub.Order;
import com.tify.back.model.users.User;
import com.tify.back.model.wish.Wish;
import com.tify.back.repository.customerservice.FAQRepository;
import com.tify.back.repository.customerservice.QnARepository;
import com.tify.back.repository.gifthub.GiftRepository;
import com.tify.back.repository.gifthub.OrderRepository;
import com.tify.back.repository.users.UserRepository;
import com.tify.back.repository.wish.WishRepository;
import com.tify.back.service.gifthub.CartService;
import com.tify.back.service.gifthub.OrderService;
import com.tify.back.service.users.UserService;
import com.tify.back.service.wish.WishService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final WishRepository wishRepository;
    private final WishService wishService;
    private final CartService cartService;
    private final OrderService orderService;
    private final GiftRepository giftRepository;
    private final QnARepository qnARepository;
    private final FAQRepository fAQRepository;
    private final OrderRepository orderRepository;
//    @GetMapping("/userlist")
//    public List<User> getUserList() {}

    //    @PostAuthorize("permitAll()")
    @Operation(summary = "admin's user creation", description = "????????? ?????? ????????????")
    @PreAuthorize("permitAll()")
    @PostMapping("/user")
    public User createUser(@RequestBody createUserDto dto) {
    //		User user = dto.toEntity();
        User user = dto.toEntity();
        userService.save(user);
        Cart cart = new Cart();
        cart.setUser(user);
        cartService.saveCart(cart);
        user.setCart(cart);
        return userService.save(user);
    }

    @Operation(summary = "admin page userList", description = "?????????????????? ????????????")
    @GetMapping("/user")
    public  Page<UserListMap> getUserList(@RequestParam(value = "page", required = false) Integer page,
                                          @RequestParam(value = "max_result", required = false) Integer max_result) {
        if (page == null) { page = 0; }
        if (max_result == null) {max_result = 0; }
        Pageable pageable = PageRequest.of(page, Math.max(10, max_result));
        return userRepository.findAllUsers(pageable);
    }

    @GetMapping("/usersearch/{email}")
    public  Page<UserListMap> searchUserListByEmail(@RequestParam(value = "page", required = false) Integer page,
                                          @RequestParam(value = "max_result", required = false) Integer max_result,
                                        @PathVariable String email) {
        if (page == null) { page = 0; }
        if (max_result == null) {max_result = 0; }
        Pageable pageable = PageRequest.of(page, Math.max(10, max_result));
        return userRepository.findAllUsersByEmail(pageable, email);
    }

    @Operation(summary = "admin page userDetail", description = "?????????????????? ????????????")
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }
    @Operation(summary = "admin page edit user info", description = "?????????????????? ?????? ?????? ??????")
    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User existedUser = userRepository.findById(id).orElse(null);
        existedUser.setUserid(user.getUserid());
        existedUser.setUsername(user.getUsername());
        existedUser.setRoles(user.getRoles());
        existedUser.setNickname(user.getNickname());
        existedUser.setBirth(user.getBirth());
        existedUser.setBirthYear(user.getBirthYear());
        existedUser.setTel(user.getTel());
        existedUser.setAddr1(user.getAddr1());
        existedUser.setAddr2(user.getAddr2());
        existedUser.setZipcode(user.getZipcode());
        existedUser.setGender(user.getGender());
        existedUser.setEmail(user.getEmail());
        existedUser.setEmailAuth(user.getEmailAuth());
        existedUser.setProvider(user.getProvider());
        existedUser.setProfileImg(user.getProfileImg());
        return userService.save(existedUser);
    }

    @Operation(summary = "admin page delete user", description = "?????????????????? ?????? ??????")
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        userRepository.deleteById(id);
        return user.getId() + " " + user.getEmail() + " user successfully deleted";
    }

    @Operation(summary = "admin page wishList", description = "?????????????????? wish ??????")
    @GetMapping("/wish")
    public Page<Wish> getWishList(@RequestParam(value = "page", required = false) Integer page,
                                  @RequestParam(value = "max_result", required = false) Integer max_result) {
        if (page == null) { page = 0; }
        if (max_result == null) {max_result = 0; }
        Pageable pageable = PageRequest.of(page, Math.max(10, max_result));
        return wishRepository.findAllWishes(pageable);
    }

    @Operation(summary = "admin page search wishList", description = "?????????????????? wish ??????")
    @GetMapping("/wishsearch/{search}")
    public Page<Wish> getWishList(@RequestParam(value = "page", required = false) Integer page,
                                  @RequestParam(value = "max_result", required = false) Integer max_result,
                                  @PathVariable String search) {
        if (page == null) { page = 0; }
        if (max_result == null) {max_result = 0; }
        Pageable pageable = PageRequest.of(page, Math.max(10, max_result));
        return wishRepository.findByUserEmailContaining(search, pageable);
    }

    @Operation(summary = "admin page wish detail", description = "?????????????????? wish detail")
    @GetMapping("/wish/{id}")
    public Wish getWishDetail(@PathVariable Long id) {
        return wishRepository.findById(id).orElse(null);
    }

    @PutMapping("/wish/{id}")
    public Wish updateWish(@PathVariable Long id, @RequestBody EditWishDto dto) {
        System.out.println(dto.getTitle());
        System.out.println(dto.getAddr2());
        System.out.println("--------------------------");
        Wish existedWish = wishRepository.findById(id).orElse(null);
        existedWish.setTotPrice(dto.getTotPrice());
        existedWish.setNowPrice(dto.getNowPrice());
        existedWish.setTitle(dto.getTitle());
        existedWish.setContent(dto.getContent());
        existedWish.setFinishYN(dto.getFinishYN());
        existedWish.setEndDate(dto.getEndDate());
        existedWish.setCardImageCode(dto.getCardImageCode());
        existedWish.setAddr1(dto.getAddr1());
        existedWish.setAddr2(dto.getAddr2());
        existedWish.setZipCode(dto.getZipCode());
        existedWish.setCategory(dto.getCategory());
        return wishRepository.save(existedWish);
    }
    @Operation(summary = "admin page delete wish", description = "?????????????????? wish ??????")
    @DeleteMapping("/wish/{id}")
    public String deleteWish(@PathVariable Long id) {
        return wishService.deleteWishById(id);
    }

    // ???????????? product api ??????. ????????? ????????? ???
    // qna, faq??? ?????? api ??????
    //

    /**
     * ???????????????, ???????????? ??????
     */
    @PostMapping("/orderState")
    private ResponseEntity<?> updateDeliverState(@RequestBody OrderStateDto dto) {
        Order order = orderService.updateDeliverState(dto);
        System.out.println("?????? ??????:"+order);
        return ResponseEntity.ok().body(order);
    }

    @Operation(summary = "admin page gift search ", description = "?????????????????? gift search")
    @GetMapping("/giftsearch/{search}")
    public Page<Gift> getGiftSearch(@RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "max_result", required = false) Integer max_result,
                                    @PathVariable String search) {
        if (page == null) { page = 0; }
        if (max_result == null) {max_result = 0; }
        Pageable pageable = PageRequest.of(page, Math.max(10, max_result));
        return giftRepository.findByGiftnameContaining(search,pageable);
    }

    @Operation(summary = "admin page qna search ", description = "?????????????????? qna search")
    @GetMapping("/qnasearch/{search}")
    public Page<QnA> getQnASearch(@RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "max_result", required = false) Integer max_result,
                                   @PathVariable String search) {
        if (page == null) { page = 0; }
        if (max_result == null) {max_result = 0; }
        Pageable pageable = PageRequest.of(page, Math.max(10, max_result));
        return qnARepository.findByUserEmailContainingOrTitleContaining(search,pageable);
    }

    @Operation(summary = "admin page faq search ", description = "?????????????????? faq search")
    @GetMapping("/faqsearch/{search}")
    public Page<FAQ> getFAQSearch(@RequestParam(value = "page", required = false) Integer page,
                                  @RequestParam(value = "max_result", required = false) Integer max_result,
                                  @PathVariable String search) {
        if (page == null) { page = 0; }
        if (max_result == null) {max_result = 0; }
        Pageable pageable = PageRequest.of(page, Math.max(10, max_result));
        return fAQRepository.findFAQByTitle(search, pageable);
    }

    @Operation(summary = "admin page order search ", description = "?????????????????? order search")
    @GetMapping("/ordersearch/{email}")
    public Page<Order> getOrderSearch(@RequestParam(value = "page", required = false) Integer page,
                                  @RequestParam(value = "max_result", required = false) Integer max_result,
                                  @PathVariable String email) {
        if (page == null) { page = 0; }
        if (max_result == null) {max_result = 0; }
        Pageable pageable = PageRequest.of(page, Math.max(10, max_result), Sort.by("createdTime").ascending());

        return orderRepository.findByUserEmail(email, pageable);
    }

    @Operation(summary = "admin page order", description = "?????????????????? order")
    @GetMapping("/order")
    public Page<Order> getOrderSearch(@RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "max_result", required = false) Integer max_result) {
        if (page == null) { page = 0; }
        if (max_result == null) {max_result = 0; }
        Pageable pageable = PageRequest.of(page, Math.max(10, max_result), Sort.by("createdTime").ascending());
        return orderRepository.findAll(pageable);
    }

}

