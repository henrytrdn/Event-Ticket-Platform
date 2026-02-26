## Create Event
POST /api/v1/events
Request Body: Event object

## List Events - Dashboard
GET /api/v1/events

## Retrieve Event
GET /api/v1/events/{event_id}

## Update Event
PUT /api/v1/events/{event_id}
Request Body: Event object

## Delete Event
DELETE /api/v1/events/{event_id}

## List Ticket Sales
GET /api/v1/events/{event_id}/tickets

## Retrieve Ticket Sale
GET /api/v1/events/{event_id}/tickets/{ticket_id}

## Partial Update
PATCH /api/v1/events/{event_id}/tickets
Request Body: Partial Ticket (TicketStatusEnum)

## List Ticket Type
GET /api/v1/events/{event_id}/ticket-types

## Retrieve Ticket Type
GET /api/v1/events/{event_id}/ticket-types/{ticket-types_id}

## Partial Update Ticket Type
PATCH /api/v1/events/{event_id}/ticket-types/{ticket-types_id}
Request Body: Partial Ticket Type (name, totalAvailable)

## Delete Ticket Type
DELETE /api/v1/events/{event_id}/ticket-types/{ticket-types_id}

## TODO: Dedicated endpoint for report data

