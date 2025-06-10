package org.ticketService.parsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TicketParserFactory {
    private final Map<String, TicketParser> parsers = new HashMap<>();

    @Autowired
    public TicketParserFactory(List<TicketParser> parserList) {
        for (TicketParser parser : parserList) {
            parsers.put(parser.getSourceName(), parser);
        }
    }

    public TicketParser getParser(String sourceName) {
        return parsers.get(sourceName);
    }
}
