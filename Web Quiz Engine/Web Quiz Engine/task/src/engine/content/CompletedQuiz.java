package engine.content;

import engine.quiz.CompletionMap;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;

public class CompletedQuiz {

    private List<CompletionMap> content = new ArrayList<>();
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
    private boolean last = true;
    private boolean first = true;
    private boolean empty = false;
    private Sort sort;

    public CompletedQuiz() {}

    public CompletedQuiz(Integer pageNumber, Integer pageSize, Sort sort) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public List<CompletionMap> getContent() {
        return content;
    }

    public void setContent(List<CompletionMap> content) {
        this.content.addAll(content);
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
