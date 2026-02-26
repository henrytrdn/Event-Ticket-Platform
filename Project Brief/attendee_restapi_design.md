## Search Published Events - Dashboard
GET /api/v1/published-events

* We separate this from the endpoint for the organizer as the events they make may not be ready to be published and/or may contain sensitive data that shouldn't be released. 
* Hence we shouldn't make the events listed homogenous for the attendee and organizer

## Retrieve Published Events
GET /api/v1/published-events/{published-event_id}

* Nest the Ticket Types for the published events in the returned published event 

## Purchase Ticket 
POST /api/v1/published-events/{published-event_id}/ticket-types/{ticket-types_id}

## List Tickets (for user)
GET /api/v1/tickets

## Retrieve Tickets (for user)
GET /api/v1/tickets/{ticket_id}

## Retrieve Tickets QR Code
GET /api/v1/tickets/{ticket_id}/qr-codes