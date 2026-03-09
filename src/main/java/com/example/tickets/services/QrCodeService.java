package com.example.tickets.services;

import com.example.tickets.domain.entities.QrCode;
import com.example.tickets.domain.entities.Ticket;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);
}
