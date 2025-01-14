package author;
import java.util.List;

public class Author {
    private String name;
    private List<String> works;

    public Author(String name, List<String> works) {
        this.name = name;
        this.works = works;
    }

    public String getName() {
        return name;
    }

    public List<String> getWorks() {
        return works;
    }

    public void addWork(String work) {
        works.add(work);
    }

}
