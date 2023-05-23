package com.hotdog.ctbs.controller.customer;

import com.hotdog.ctbs.entity.LoyaltyPoint;
import com.hotdog.ctbs.repository.LoyaltyPointRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The {@code LoyaltyPointReadController} class exposes
 * the {@code /api/customer/loyalty-point/read} endpoint.
 * <p />
 *
 * The returned JSON format is:
 * <blockquote><pre>
 * [
 *   {
 *     "username":        "copperfield",
 *     "pointsAvailable": "120",
 *     "pointsRedeemed":  "80",
 *     "pointsTotal":     "200"
 *   },
 *   {
 *     "username":        "blaine",
 *     "pointsAvailable": "200",
 *     "pointsRedeemed":  "0",
 *     "pointsTotal":     "200"
 *   }
 * ]
 * </pre></blockquote>
 *
 * A singular UserAccount object will NOT be wrapped in an array:
 * <blockquote><pre>
 * {
 *   "username":        "blaine",
 *   ...
 *   "pointsTotal":     "200"
 * }
 * </pre></blockquote>
 *
 * @author Baraq Kamsani
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/customer/loyalty-point")
public class CustomerLoyaltyPointReadController {

    private final LoyaltyPointRepository loyaltyPointRepo;

    public CustomerLoyaltyPointReadController(LoyaltyPointRepository loyaltyPointRepo)
    {
        this.loyaltyPointRepo = loyaltyPointRepo;
    }

    @GetMapping("/read/{param}")
    public ResponseEntity<String> Read(@PathVariable String param)
    {
        try {
            String json = LoyaltyPoint.readLoyaltyPoint(loyaltyPointRepo, param);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
