package CacheSmartCatalog.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
