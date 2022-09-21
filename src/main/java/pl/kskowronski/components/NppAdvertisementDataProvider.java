package pl.kskowronski.components;

import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import pl.kskowronski.data.entity.admin.NppAdvertisement;
import pl.kskowronski.data.entity.admin.NppSkForSupervisor;
import pl.kskowronski.data.service.admin.NppAdvertisementService;
import pl.kskowronski.data.service.admin.NppSkForSupervisorService;
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.egeria.global.NapUserService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class NppAdvertisementDataProvider extends AbstractBackEndDataProvider<NppAdvertisement, CrudFilter> {

    // A real app should hook up something like JPA
    List<NppAdvertisement> DATABASE;
    private NppAdvertisementService nppAdvertisementService;
    private Consumer<Long> sizeChangeListener;


    public NppAdvertisementDataProvider(NppAdvertisementService nppAdvertisementService) {
        this.nppAdvertisementService = nppAdvertisementService;
        DATABASE = new ArrayList<>(nppAdvertisementService.findAll());
        DATABASE.stream().forEach( item -> {
            item.setLp(item.getLp());
            item.setText(item.getText());
        });
    }

    private static Predicate<NppAdvertisement> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<NppAdvertisement>) adver -> {
                    try {
                        Object value = valueOf(constraint.getKey(), adver);
                        return value != null && value.toString().toLowerCase()
                                .contains(constraint.getValue().toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .reduce(Predicate::and)
                .orElse(e -> true);
    }

    private static Comparator<NppAdvertisement> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream()
                .map(sortClause -> {
                    try {
                        Comparator<NppAdvertisement> comparator = Comparator.comparing(adver ->
                                (Comparable) valueOf(sortClause.getKey(), adver)
                        );

                        if (sortClause.getValue() == SortDirection.DESCENDING) {
                            comparator = comparator.reversed();
                        }

                        return comparator;

                    } catch (Exception ex) {
                        return (Comparator<NppAdvertisement>) (o1, o2) -> 0;
                    }
                })
                .reduce(Comparator::thenComparing)
                .orElse((o1, o2) -> 0);
    }

    private static Object valueOf(String fieldName, NppAdvertisement adver) {
        try {
            Field field = NppAdvertisement.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(adver);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Stream<NppAdvertisement> fetchFromBackEnd(Query<NppAdvertisement, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Stream<NppAdvertisement> stream = DATABASE.stream();

        if (query.getFilter().isPresent()) {
            stream = stream
                    .filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        }

        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<NppAdvertisement, CrudFilter> query) {
        // For RDBMS just execute a SELECT COUNT(*) ... WHERE query
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }

        return (int) count;
    }

    void setSizeChangeListener(Consumer<Long> listener) {
        sizeChangeListener = listener;
    }

    public void persist(NppAdvertisement item) {
        if (item.getId() == null) {
            Comparator<NppAdvertisement> comparator = Comparator.comparing( NppAdvertisement::getId );
            Integer max = 0;
            if ( DATABASE.stream().max(comparator).isPresent()) {
                max = DATABASE.stream().max(comparator).get().getId();
            } else {
                max = 0;
            }
            item.setId(max + 1);
        }

        final Optional<NppAdvertisement> existingItem = find(item.getId());
        if (existingItem.isPresent()) {
            int position = DATABASE.indexOf(existingItem.get());
            DATABASE.remove(existingItem.get());
            nppAdvertisementService.deleteById(item.getId());
            DATABASE.add(position, item);
            nppAdvertisementService.save(item);
        } else {
            DATABASE.add(item);
            nppAdvertisementService.save(item);
        }
    }

    Optional<NppAdvertisement> find(Integer id) {
        return DATABASE
                .stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    public void delete(NppAdvertisement item) {
        nppAdvertisementService.deleteById(item.getId());
        DATABASE.removeIf(entity -> entity.getId().equals(item.getId()));
    }
}
