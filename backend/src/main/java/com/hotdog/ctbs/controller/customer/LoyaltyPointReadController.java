package com.hotdog.ctbs.controller.customer;

/*
TODO: convert this controller to the new format and remove its Implementation class.
*/


// Application imports.
import com.hotdog.ctbs.entity.LoyaltyPoint;
import com.hotdog.ctbs.service.implementation.LoyaltyPointImpl;

// Java imports.
import java.util.List;

// JSON serialization imports.
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Spring imports.
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class LoyaltyPointReadController {

    private final LoyaltyPointImpl loyaltyPointImpl;
    private final ObjectMapper objectMapper;

    public LoyaltyPointReadController(LoyaltyPointImpl loyaltyPointImpl)
    {
        this.loyaltyPointImpl = loyaltyPointImpl;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /** Read a JSON array of {@code UserAccount} object(s). */
    @GetMapping("/read/{param}")
    public String ReadAvailable(@PathVariable String param)
    {
        try {
            List<LoyaltyPoint> loyaltyPointList = switch (param) {
                case "all" ->
                        loyaltyPointImpl.getAllLoyaltyPoints();
                case "active" ->
                        loyaltyPointImpl.getActiveLoyaltyPoints();
                default ->
                        List.of(loyaltyPointImpl.getLoyaltyPointByUsername(param));
            };
            JsonNode[] jsonNodes = new JsonNode[loyaltyPointList.size()];
            ArrayNode arrayNode = objectMapper.createArrayNode();
            for (int i = loyaltyPointList.size() - 1; i >= 0; i--) {
                LoyaltyPoint loyaltyPoint = loyaltyPointList.get(i);
                String username = loyaltyPoint.getUserAccount().getUsername();
                int pointsAvailable = loyaltyPointImpl.getAvailablePoint(loyaltyPoint);
                jsonNodes[i] = objectMapper.valueToTree(loyaltyPoint);
                ((ObjectNode) jsonNodes[i]).remove("id");
                ((ObjectNode) jsonNodes[i]).remove("userAccount");
                ((ObjectNode) jsonNodes[i]).put("username", username);
                ((ObjectNode) jsonNodes[i]).put("pointsAvailable", pointsAvailable);
                arrayNode.add(jsonNodes[i]);
            }
            // if size > 1 then [ { ... }, { ... } ] else { ... }
            return objectMapper.writeValueAsString(
                    arrayNode.size() > 1 ? arrayNode : arrayNode.get(0)
            );
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
