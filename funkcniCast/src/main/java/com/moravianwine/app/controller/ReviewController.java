package com.moravianwine.app.controller;

import com.moravianwine.app.dto.WineIdentityMap;
import com.moravianwine.app.model.Review;
import com.moravianwine.app.model.Users;
import com.moravianwine.app.model.Wine;
import com.moravianwine.app.repository.ReviewgateWay;
import com.moravianwine.app.service.ReviewDomain;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.Collection;

@Controller
public class ReviewController {

    private final ReviewgateWay reviewgateWay;
    private final WineIdentityMap wineIdentityMap;
    public ReviewController(ReviewgateWay reviewgateWay, WineIdentityMap wineIdentityMap) {
        this.reviewgateWay = reviewgateWay;
        this.wineIdentityMap = wineIdentityMap;

    }
    @GetMapping("/reviews/{wineId}")
    public String getReviewsForWine(@PathVariable int wineId, Model model) throws SQLException {
        Wine wine = wineIdentityMap.getWine(wineId);
        if (wine.getReviews() == null ){
            wine.addReviewHolder(reviewgateWay.createReviewLoader(wine));
            System.out.println("Recenze z databaze");
        }
        Collection<Review> reviews = wine.getReviewsHolder();
        System.out.println(reviews);
        model.addAttribute("wine", wine);
        model.addAttribute("reviews", reviews);
        model.addAttribute("newReview", new Review());
        return "reviews";
    }
    @PostMapping("/addReview")
    public String addReview(@ModelAttribute("newReview") Review review, @RequestParam("w") int wineId, HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
        Users user = (Users) session.getAttribute("currentUser");
        try {
            ReviewDomain reviewDomain = new ReviewDomain(reviewgateWay);
            reviewDomain.createReview(user,wineIdentityMap.getWine(wineId),review.getComment(),review.getRating());
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/reviews/" + wineId;
    }
}
