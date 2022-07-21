package pl.kskowronski.views.admin;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kskowronski.data.entity.admin.NppSkForSupervisor;
import pl.kskowronski.data.entity.egeria.css.SK;
import pl.kskowronski.data.entity.egeria.global.NapUser;
import pl.kskowronski.data.service.admin.NppSkForSupervisorService;
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.egeria.global.NapUserService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.List;


@Route(value = "sk-for-supervisor", layout = MainLayout.class)
@PageTitle("SkForSupervisorView")
@RolesAllowed("admin")
public class SkForSupervisorView extends Div {


    private NppSkForSupervisorService nppSkForSupervisorService;
    private SKService skService;
    private NapUserService napUserService;

    private Crud<NppSkForSupervisor> crud;

    private String PRC_NAZWISKO_IMIE = "nazwImie";
    private String SK_KOD = "skKod";
    private String EDIT_COLUMN = "vaadin-crud-edit-column";

    @Autowired
    public SkForSupervisorView(NppSkForSupervisorService nppSkForSupervisorService, SKService skService, NapUserService napUserService) {
        this.nppSkForSupervisorService = nppSkForSupervisorService;
        this.skService = skService;
        this.napUserService = napUserService;
        setHeight("100%");
        crud = new Crud<>(
                NppSkForSupervisor.class,
                createEditor()
        );
        crud.setHeightFull();



        setupGrid();
        setupDataProvider();

        add(crud);
    }

    private CrudEditor<NppSkForSupervisor> createEditor() {

        ComboBox<NapUser> selectUser = getSelectUser();
        ComboBox<SK> selectSkKod = getSelectSK();

        FormLayout form = new FormLayout(selectUser, selectSkKod);

        Binder<NppSkForSupervisor> binder = new Binder<>(NppSkForSupervisor.class);
        binder.forField(selectUser).asRequired().bind(NppSkForSupervisor::getNapUser, NppSkForSupervisor::setNapUser);
        binder.forField(selectSkKod).asRequired().bind(NppSkForSupervisor::getSk, NppSkForSupervisor::setSk);


        return new BinderCrudEditor<>(binder, form);
    }

    private void setupGrid() {
        Grid<NppSkForSupervisor> grid = crud.getGrid();

        // Only show these columns (all columns shown by default):
        List<String> visibleColumns = Arrays.asList(
                PRC_NAZWISKO_IMIE,
                SK_KOD,
                EDIT_COLUMN
        );
        grid.getColumns().forEach(column -> {
            String key = column.getKey();
            if (!visibleColumns.contains(key)) {
                grid.removeColumn(column);
            }
        });

        // Reorder the columns (alphabetical by default)
        grid.setColumnOrder(
                grid.getColumnByKey(PRC_NAZWISKO_IMIE),
                grid.getColumnByKey(SK_KOD),
                grid.getColumnByKey(EDIT_COLUMN)
        );
    }

    private void setupDataProvider() {
        pl.kskowronski.component.SkForSupervisorDataProvider dataProvider = new pl.kskowronski.component.SkForSupervisorDataProvider(nppSkForSupervisorService, skService, napUserService);
        crud.setDataProvider(dataProvider);
        crud.addDeleteListener(deleteEvent ->
                dataProvider.delete(deleteEvent.getItem())
        );
        crud.addSaveListener(saveEvent ->
                dataProvider.persist(saveEvent.getItem())
        );
    }



    private ComboBox<SK> getSelectSK() {
        ComboBox<SK> comboSK = new ComboBox<>();
        comboSK.setItems( query ->
                skService.findAll("%" + query.getFilter().orElse("") + "%",query.getPage(),query.getPageSize())
        );
        comboSK.setItemLabelGenerator(SK::getSkKod);
        comboSK.setLabel("Obiekt");
        return comboSK;
    }

    private ComboBox<NapUser> getSelectUser() {
        ComboBox<NapUser> selectUser = new ComboBox<>();
        selectUser.setItems( query -> napUserService.findAll("%" + query.getFilter().orElse("") + "%",query.getPage(),query.getPageSize()));
        selectUser.setItemLabelGenerator(NapUser::getUsername);
        //selectSK.setEmptySelectionCaption(listSK.get(0).getSkKod());
        selectUser.setLabel("Kierownik");
        return selectUser;
    }

}
