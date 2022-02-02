package pl.kskowronski.component;

import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import pl.kskowronski.data.entity.admin.NppSkForSupervisor;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import pl.kskowronski.data.service.UserService;
import pl.kskowronski.data.service.admin.NppSkForSupervisorService;
import pl.kskowronski.data.service.egeria.css.SKService;
import pl.kskowronski.data.service.egeria.global.NapUserService;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;



public class SkForSupervisorDataProvider extends AbstractBackEndDataProvider<NppSkForSupervisor, CrudFilter> {

    // A real app should hook up something like JPA
    List<NppSkForSupervisor> DATABASE;
    private NppSkForSupervisorService nppSkForSupervisorService;
    private Consumer<Long> sizeChangeListener;


    public SkForSupervisorDataProvider(NppSkForSupervisorService nppSkForSupervisorService, SKService skService, NapUserService napUserService) {
        this.nppSkForSupervisorService = nppSkForSupervisorService;
        DATABASE = new ArrayList<>(nppSkForSupervisorService.findAll());
        DATABASE.stream().forEach( item -> {
            item.setNapUser(napUserService.findById(item.getPrcId()).get());
            item.setSk(skService.findBySkKod(item.getSkKod()));
        });
    }

    private static Predicate<NppSkForSupervisor> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<NppSkForSupervisor>) person -> {
                    try {
                        Object value = valueOf(constraint.getKey(), person);
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

    private static Comparator<NppSkForSupervisor> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream()
                .map(sortClause -> {
                    try {
                        Comparator<NppSkForSupervisor> comparator = Comparator.comparing(person ->
                                (Comparable) valueOf(sortClause.getKey(), person)
                        );

                        if (sortClause.getValue() == SortDirection.DESCENDING) {
                            comparator = comparator.reversed();
                        }

                        return comparator;

                    } catch (Exception ex) {
                        return (Comparator<NppSkForSupervisor>) (o1, o2) -> 0;
                    }
                })
                .reduce(Comparator::thenComparing)
                .orElse((o1, o2) -> 0);
    }

    private static Object valueOf(String fieldName, NppSkForSupervisor person) {
        try {
            Field field = NppSkForSupervisor.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(person);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Stream<NppSkForSupervisor> fetchFromBackEnd(Query<NppSkForSupervisor, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Stream<NppSkForSupervisor> stream = DATABASE.stream();

        if (query.getFilter().isPresent()) {
            stream = stream
                    .filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        }

        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<NppSkForSupervisor, CrudFilter> query) {
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

    public void persist(NppSkForSupervisor item) {
        if (item.getId() == null) {
            Comparator<NppSkForSupervisor> comparator = Comparator.comparing( NppSkForSupervisor::getId );
            Integer max = DATABASE.stream().max(comparator).get().getId();
            item.setId(max + 1);
        }

        final Optional<NppSkForSupervisor> existingItem = find(item.getId());
        if (existingItem.isPresent()) {
            int position = DATABASE.indexOf(existingItem.get());
            DATABASE.remove(existingItem.get());
            nppSkForSupervisorService.deleteById(item.getId());
            DATABASE.add(position, item);
            nppSkForSupervisorService.save(item);
        } else {
            DATABASE.add(item);
            nppSkForSupervisorService.save(item);
        }
    }

    Optional<NppSkForSupervisor> find(Integer id) {
        return DATABASE
                .stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    public void delete(NppSkForSupervisor item) {
        nppSkForSupervisorService.deleteById(item.getId());
        DATABASE.removeIf(entity -> entity.getId().equals(item.getId()));
    }
}
