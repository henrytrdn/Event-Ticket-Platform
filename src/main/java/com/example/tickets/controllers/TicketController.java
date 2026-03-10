package com.example.tickets.controllers;

import com.example.tickets.domain.dtos.GetTicketResponseDto;
import com.example.tickets.domain.dtos.ListTicketResponseDto;
import com.example.tickets.mappers.TicketMapper;
import com.example.tickets.services.QrCodeService;
import com.example.tickets.services.TicketService;
import com.example.tickets.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final QrCodeService qrCodeService;

    @GetMapping
    public Page<ListTicketResponseDto> listTickets(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
    ) {
        return ticketService.listTicketsForUser(
                JwtUtil.parseUserId(jwt),
                pageable
        ).map(ticket -> ticketMapper.toListTicketResponseDto(ticket));
    }

    @GetMapping(path = "/{ticketId}")
    public ResponseEntity<GetTicketResponseDto> getTicket(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketId
    ) {
        return ticketService
                .getTicketForUser(JwtUtil.parseUserId(jwt), ticketId)
                .map(ticket -> ticketMapper.toGetTicketResponseDto(ticket))
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{ticketId}/qr-codes")
    public ResponseEntity<byte[]> getTicketQrCode(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketId
    ) {
        byte[] qrCodeImage = qrCodeService.getQrCodeImageForUserAndTicket(
                JwtUtil.parseUserId(jwt),
                ticketId
        );

        HttpHeaders headers = new HttpHeaders();    // HTTP responses have a header (metadata) and a body (data)
        headers.setContentType(MediaType.IMAGE_PNG);    // sets the HTTP header to display content type as PNG to display an image
        headers.setContentLength(qrCodeImage.length); // Tells the client how many bytes long the body is

        return ResponseEntity.ok()
                .headers(headers)
                .body(qrCodeImage);
    }

    // example http response
    // Status: 200 OK
    //Headers:
    //   Content-Type: image/png
    //   Content-Length: 5342
    //Body:
    //   [binary QR code image]

}
