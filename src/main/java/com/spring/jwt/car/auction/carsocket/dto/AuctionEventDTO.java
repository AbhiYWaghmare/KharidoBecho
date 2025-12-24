<<<<<<< HEAD

<<<<<<< HEAD:src/main/java/com/spring/jwt/car/auction/carsocket/dto/AuctionEventDTO.java
package com.spring.jwt.car.auction.carsocket.dto;
=======
=======
>>>>>>> cfb28e11e2778507189739031086abecc0048ee0
package com.spring.jwt.socket.dto;
>>>>>>> 3f6fd5271690c6d33a58f5b7773addd3ba9a6e3d:src/main/java/com/spring/jwt/socket/dto/AuctionEventDTO.java

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AuctionEventDTO(String type, Long auctionId, Long listingId,
                              BigDecimal amount, Long bidderUserId,
                              OffsetDateTime timestamp, Object extra) { }
