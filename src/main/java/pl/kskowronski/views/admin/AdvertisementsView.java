package pl.kskowronski.views.admin;

import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kskowronski.components.NppAdvertisementDataProvider;
import pl.kskowronski.data.entity.admin.NppAdvertisement;
import pl.kskowronski.data.entity.admin.NppSkForSupervisor;
import pl.kskowronski.data.service.admin.NppAdvertisementService;
import pl.kskowronski.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.List;

@Route(value = "advertisements", layout = MainLayout.class)
@PageTitle("AdvertisementsView")
@RolesAllowed({"admin","hr_manager"})
public class AdvertisementsView extends Div  {

    private NppAdvertisementService nppAdvertisementService;

    private Crud<NppAdvertisement> crud;

    private String LP = "lp";
    private String TEXT = "text";
    private String EDIT_COLUMN = "vaadin-crud-edit-column";

    @Autowired
    public AdvertisementsView(NppAdvertisementService nppAdvertisementService) {
        this.nppAdvertisementService = nppAdvertisementService;
        setHeight("100%");

        crud = new Crud<>(
                NppAdvertisement.class,
                createEditor()
        );
        crud.setHeightFull();

        setupGrid();
        setupDataProvider();

        add(crud);
    }

    private CrudEditor<NppAdvertisement> createEditor() {

        TextField textLp = new TextField();
        TextArea textText = new TextArea();

        FormLayout form = new FormLayout(textLp, textText);

        Binder<NppAdvertisement> binder = new Binder<>(NppAdvertisement.class);
        binder.forField(textLp).asRequired().bind(NppAdvertisement::getLp, NppAdvertisement::setLp);
        binder.forField(textText).asRequired().bind(NppAdvertisement::getText, NppAdvertisement::setText);


        return new BinderCrudEditor<>(binder, form);
    }


    private void setupGrid() {
        Grid<NppAdvertisement> grid = crud.getGrid();

        // Only show these columns (all columns shown by default):
        List<String> visibleColumns = Arrays.asList(
                LP,
                TEXT,
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
                grid.getColumnByKey(LP),
                grid.getColumnByKey(TEXT),
                grid.getColumnByKey(EDIT_COLUMN)
        );
    }

    private void setupDataProvider() {
        NppAdvertisementDataProvider dataProvider = new NppAdvertisementDataProvider(nppAdvertisementService);
        crud.setDataProvider(dataProvider);
        crud.addDeleteListener(deleteEvent ->
                dataProvider.delete(deleteEvent.getItem())
        );
        crud.addSaveListener(saveEvent ->
                dataProvider.persist(saveEvent.getItem())
        );
    }
}
