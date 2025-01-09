package com.moravianwine.app.controller;

import com.moravianwine.app.dto.WineUnitOfWork;
import com.moravianwine.app.model.*;
import com.moravianwine.app.repository.*;
import com.moravianwine.app.repository.FavoriteWines.FavoriteMapper;
import com.moravianwine.app.service.CartDomain;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableJpaRepositories
@Controller
@AllArgsConstructor
public class WineController {

    private final WineryMapper wineryMapper; // Mapper pro vinařství
    private final RegionGateWay regionGateWay;
    private final WineUnitOfWork Wuow;
    private final FavoriteMapper favoriteMapper;
    private final CartDomain cartDomain;
    private final UserMapper userMapper;
    private final Cart userCart;

    @GetMapping("/wines")
    public String getAllWines(HttpSession session,Model model) throws SQLException {
        Authentication at = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) at.getPrincipal();

        Users currentUser = userMapper.findByUsername(userDetails.getUsername());
        String roleString = String.valueOf(currentUser.getRole());
        Cart cart = cartDomain.findOrCreateCart(currentUser.getUserId());
        currentUser.setCart(cart);
        System.out.println(cart);
        Collection<Wine> wines = Wuow.getWineIdentityMap().getAllWines().values();
       // wines.forEach(wine -> System.out.println("Wine: " + wine));

        String errorMessage = (String) session.getAttribute("errorMessage");
        session.removeAttribute("errorMessage");

        model.addAttribute("userRoleString", roleString);
        model.addAttribute("wines", wines);
        model.addAttribute("user", currentUser);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("cart", cart);
        session.setAttribute("currentUser",currentUser);
        session.setAttribute("cart", cart);
        return "wines";
    }

    @GetMapping("/add-wine")
    public String showAddWineForm(HttpSession session,Model model) throws SQLException {
        Users currentUser = (Users) session.getAttribute("currentUser");
        if(currentUser.getRole() == 0) {
            throw new RuntimeException("You are not admin");
        }
        model.addAttribute("wine", new Wine()); // Přidání prázdného objektu Wine
        List<Winery> wineries = wineryMapper.findAll();
        List<Region> regions = regionGateWay.findAll();
        model.addAttribute("wineries", wineries);
        model.addAttribute("regions", regions);
        model.addAttribute("types", List.of("Červené", "Bílé", "Růžové"));
        model.addAttribute("pendingWines", Wuow.getNewObjects()); // Vína čekající na commit
        return "add-wine";
    }
    @PostMapping("/add-wine")
    public String addWine(@ModelAttribute("wine") Wine wine,Model model) throws SQLException {
        wine.setWinery(wineryMapper.findById(wine.getWinery().getWineryId()));
        wine.setRegion(regionGateWay.findById(wine.getRegion().getRegionId()));

        try {
            Wuow.addWine(wine);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "add-wine"; // Vrátí stránku s chybovou zprávou
        }
        return "redirect:/add-wine";
    }
    @PostMapping("/commit-wines")
    public String commitWines(Model model) throws SQLException {
        try {
            Wuow.commit(); // Commit všech vín do databáze
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Chyba při ukládání vín: " + e.getMessage());
            return "add-wine"; // Vrátí stránku s chybovou zprávou
        }
        return "redirect:/wines"; // Přesměrování na formulář
    }

    @PostMapping("/addToFavorite")
    @ResponseBody
    public Map<String, String> addToFavorite(
            @RequestParam("userId") int userId,
            @RequestParam("wineId") int wineId,
            HttpSession session) throws SQLException {
        Map<String,String> response = new HashMap<>();
        System.out.println("heeeeej");
        try{
            favoriteMapper.addFavorite(userId, wineId);
            response.put("status","success");
            response.put("message","FavoriteWines added");
        }catch (RuntimeException e){
            session.setAttribute("errorMessage", e.getMessage() + "Odebiram z oblibenych");
            response.put("status","error");
            response.put("message",e.getMessage());
        }
        return response;
    }

    @PostMapping("/addToCart")
    public String addToCart(
                            @RequestParam("wineId") int wineId,
                            @RequestParam("quantity") int quantity,
                            HttpSession session) throws SQLException {
        try {
            Cart cart = (Cart) session.getAttribute("cart");

            if (cart == null) {
                throw new RuntimeException("Košík nebyl nalezen.");
            }
            Wine wine = Wuow.getWineIdentityMap().getWine(wineId);
            if (wine == null) {
                throw new RuntimeException("Víno nebylo nalezeno.");
            }

            cartDomain.addWineToCart(cart,wine,quantity);


        } catch (RuntimeException e) {
            session.setAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/wines";
    }


}