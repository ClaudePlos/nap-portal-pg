package pl.kskowronski.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;

import java.text.ParseException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.kskowronski.data.MapperDate;
import pl.kskowronski.data.entity.admin.User;
import pl.kskowronski.data.entity.egeria.ek.Zatrudnienie;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.egeria.ek.ZatrudnienieService;
import pl.kskowronski.security.AuthenticatedUser;
import pl.kskowronski.views.absences.AllAboutAbsencesView;
import pl.kskowronski.views.admin.AdvertisementsView;
import pl.kskowronski.views.admin.SkForSupervisorView;
import pl.kskowronski.views.advancepayment.AdvancePaymentView;
import pl.kskowronski.views.api.ApiView;
import pl.kskowronski.views.documents.DocumentsView;
import pl.kskowronski.views.logout.Logout;
import pl.kskowronski.views.mainpage.MainPageView;
import pl.kskowronski.views.map.MapView;
import pl.kskowronski.views.payslips.PayslipsView;
import pl.kskowronski.views.payslipscontract.PayslipsContractView;
import pl.kskowronski.views.pit11.Pit11View;
import pl.kskowronski.views.pit11list.Pit11listView;
import pl.kskowronski.views.reports.ReportsView;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
public class MainLayout extends AppLayout {

    public static class MenuItemInfo {

        private String text;
        private String iconClass;
        private Class<? extends Component> view;

        public MenuItemInfo(String text, String iconClass, Class<? extends Component> view) {
            this.text = text;
            this.iconClass = iconClass;
            this.view = view;
        }

        public String getText() {
            return text;
        }

        public String getIconClass() {
            return iconClass;
        }

        public Class<? extends Component> getView() {
            return view;
        }

    }

    private H1 viewTitle;

    private VaadinSession session = VaadinSession.getCurrent();
    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    //k.skowronski beans
    private transient ZatrudnienieService zatrudnienieService;
    private transient UserService userService;
    private transient User worker;
    private transient MapperDate mapperDate = new MapperDate();

    @Autowired
    public MainLayout(UserService userService, ZatrudnienieService zatrudnienieService,
                      AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        this.zatrudnienieService = zatrudnienieService;
        this.userService = userService;

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("text-secondary");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-center",
                "w-full");
        return header;
    }

    private Component createDrawerContent() {
        HorizontalLayout h01 = new HorizontalLayout();
        H2 appName = new H2("Portal pracowniczy");
        appName.addClassNames("flex", "items-center", "h-xl", "m-0", "px-m", "text-m");

        Image logo = new Image("icons/icon-32x32.png", "portal logo");
        logo.setClassName("logoCompany");
        h01.add(logo, appName);
        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(h01,
                createNavigation(), createFooter());
        section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
        return section;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
        nav.getElement().setAttribute("aria-labelledby", "views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("list-none", "m-0", "p-0");
        nav.add(list);

        for (RouterLink link : createLinks()) {
            ListItem item = new ListItem(link);
            list.add(item);
        }
        return nav;
    }

    private List<RouterLink> createLinks() {

        // get info about sing in worker
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> workerOp = userService.findByUsername(userDetails.getUsername());
        worker = workerOp.get();
        session.setAttribute(User.class, worker);


        //check actual agreement for worker
        Optional<List<Zatrudnienie>> listContract = null;
        Optional<List<Zatrudnienie>> listContractUz = null;
        try {
            listContract = zatrudnienieService.getActualContractForWorker(worker.getPrcId(), mapperDate.dtYYYYMM.format(new Date()));
            listContractUz = zatrudnienieService.getActualContractUzForWorker(worker.getPrcId(), mapperDate.dtYYYYMM.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        MenuItemInfo[] menuItems = new MenuItemInfo[]{ //
                new MenuItemInfo("Strona główna", "la la-globe", MainPageView.class), //
                new MenuItemInfo("Twój urlop", "la la-circle-thin", AllAboutAbsencesView.class), //
                new MenuItemInfo("Twóe dokumenty", "la la-circle-thin", DocumentsView.class), //
                new MenuItemInfo("Twoje zaliczki", "la la-money", AdvancePaymentView.class), //
                new MenuItemInfo("Pit 11", "la la-circle-thin", Pit11View.class), //
                new MenuItemInfo("Paski", "la la-circle-thin", PayslipsView.class), //
                new MenuItemInfo("Paski UZ", "la la-circle-thin", PayslipsContractView.class), //
                new MenuItemInfo("Map", "la la-map", MapView.class), //
                new MenuItemInfo("Pit 11 lista", "la la-list", Pit11listView.class), //
                new MenuItemInfo("Admin(sk)", "la la-cog", SkForSupervisorView.class), //
                new MenuItemInfo("Raporty", "la la-cog", ReportsView.class), //
                new MenuItemInfo("Ogłoszenia", "la la-cog", AdvertisementsView.class), //
                new MenuItemInfo("Api", "la la-circle-thin", ApiView.class), //
                new MenuItemInfo("Wyloguj", "la la-circle-thin", Logout.class), //

                //new MenuItemInfo("About", "la la-file", AboutView.class), //
                //new MenuItemInfo("Image List", "la la-th-list", ImageListView.class), //

        };




        List<RouterLink> links = new ArrayList<>();
        for (MenuItemInfo menuItemInfo : menuItems) {
            if (accessChecker.hasAccess(menuItemInfo.getView())) {
                links.add(createLink(menuItemInfo));
            }

        }
        return links;
    }

    private static RouterLink createLink(MenuItemInfo menuItemInfo) {
        RouterLink link = new RouterLink();
        link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
        link.setRoute(menuItemInfo.getView());

        Span icon = new Span();
        icon.addClassNames("me-s", "text-l");
        if (!menuItemInfo.getIconClass().isEmpty()) {
            icon.addClassNames(menuItemInfo.getIconClass());
        }

        Span text = new Span(menuItemInfo.getText());
        text.addClassNames("font-medium", "text-s");

        link.add(icon, text);
        return link;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            int colorIndex = (int) ((Math.random() * (20 - 1)) + 1);
            Avatar avatar = new Avatar(!user.getPrcImie().isEmpty()? user.getPrcImie() + ' ' + user.getPrcNazwisko() : user.getUsername());
            avatar.setColorIndex(colorIndex);
            avatar.addClassNames("me-xs");

            ContextMenu userMenu = new ContextMenu(avatar);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Wyloguj", e -> {
                authenticatedUser.logout();
            });


            Span name = new Span(!user.getPrcImie().isEmpty()? user.getPrcImie() + ' ' + user.getPrcNazwisko() : user.getUsername());
            name.addClassNames("font-medium", "text-s", "text-secondary");

            layout.add(avatar, name);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
