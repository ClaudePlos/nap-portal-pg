package pl.kskowronski.views.reports.list;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.log.Log;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.log.LogService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class WorkersRegulationWhoGotView extends VerticalLayout {

    private Grid<LogDTO> grid;



    private List<LogDTO> listLog = new ArrayList<>();

    public WorkersRegulationWhoGotView(LogService logService, UserService userService) {
        setHeight("85%");

        this.grid = new Grid<>(LogDTO.class);
        grid.setClassName("gridLog");
        grid.setColumns();
        grid.addColumn("prcNumber").setWidth("30px").setHeader("Numer");
        grid.addColumn("name").setWidth("100px").setHeader("Nazwisko");
        grid.addColumn("surname").setWidth("100px").setHeader("Imię");
        grid.addColumn("auditDc").setWidth("100px").setHeader("Data");
        grid.addColumn("description").setWidth("200px").setHeader("Opis");

        add(grid);

        List<Log> logs = logService.findAllGroupByPrcId();
        logs.stream().forEach( log -> {
            LogDTO l = new LogDTO();
            l.setId( log.getId() );
            l.setAuditDc( log.getAuditDc() );
            l.setDescription( log.getDescription() );
            l.setPrcId( log.getPrcId());
            l.setEvent( log.getEvent());

            Optional<User> u = userService.findById(log.getPrcId());
            if (u.isPresent()) {
                l.setPrcNumber(u.get().getPrcNumer());
                l.setName(u.get().getPrcImie());
                l.setSurname(u.get().getPrcNazwisko());
            }

            listLog.add(l);
        });

        grid.setItems(listLog);

    }

    public static class LogDTO {

        private Integer id;
        private Integer prcId;
        private Integer prcNumber;
        private String name;
        private String surname;
        private String event;
        private Date auditDc;
        private String description;

        public LogDTO() {
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPrcId() {
            return prcId;
        }

        public void setPrcId(Integer prcId) {
            this.prcId = prcId;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public Date getAuditDc() {
            return auditDc;
        }

        public void setAuditDc(Date auditDc) {
            this.auditDc = auditDc;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getPrcNumber() {
            return prcNumber;
        }

        public void setPrcNumber(Integer prcNumber) {
            this.prcNumber = prcNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }
    }
}


