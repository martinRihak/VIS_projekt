package com.moravianwine.app.controller;

import com.moravianwine.app.dto.WineUnitOfWork;
import com.moravianwine.app.model.Users;
import com.moravianwine.app.model.Wine;
import com.moravianwine.app.repository.FavoriteWines.FavoriteMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor

public class FavoriteController {
    private final WineUnitOfWork Wuow;
    private final FavoriteMapper favoriteMapper;


    @GetMapping("/favorite-wines")
    public String showFavoriteWines(HttpSession session, Model model) throws SQLException {
        System.out.println("Zpracování požadavku na /favorite-Wines");
        Users user = (Users) session.getAttribute("currentUser");
        if (user == null) {
            throw new RuntimeException("Přístup odepřen: Uživatelská relace nebyla nalezena.");
        }
        List<Integer> favoriteWineIds = favoriteMapper.getFavoritesByUser(user.getUserId()); // Získání ID oblíbených vín
        List<Wine> favoriteWines = new ArrayList<>();

        // Načtení detailů vín podle jejich ID
        for (Integer wineId : favoriteWineIds) {
            Wine wine = Wuow.getWineIdentityMap().getIdentityMap().get(wineId);
            if (wine != null) {
                favoriteWines.add(wine);
            }
        }
        model.addAttribute("favoriteWines", favoriteWines); // Předání seznamu vín do modelu
        return "favorite-wines";
    }
    /*@PostMapping("/favorites/remove")
    public String removeFavorite(@RequestParam int userId, @RequestParam int wineId) {
        try {
            favoriteMapper.removeFavorite(userId, wineId);
            return "redirect:/favorites/" + userId; // Přesměrování zpět na stránku oblíbených vín
        } catch (SQLException e) {
            return "redirect:/favorites/" + userId + "?error=Chyba při odstraňování vína.";
        }
    }*/
}
