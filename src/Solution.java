import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

class Row{
    private final int x0;
    private final int x1;
    private final int y0;
    private final int y1;

    public Row(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
    }

    public int getX0() {
        return x0;
    }

    public int getX1() {
        return x1;
    }

    public int getY0() {
        return y0;
    }

    public int getY1() {
        return y1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return x0 == row.x0 && x1 == row.x1 && y0 == row.y0 && y1 == row.y1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x0, x1, y0, y1);
    }
}

public class Solution {
    private static int start;
    private static int commonSpace;

    public static int calculateSpace(int[][] rectangles) {
        if (rectangles.length == 0)
            return 0;

        commonSpace = 0;
        List<Row> list = new LinkedList<>();
        start = 0;

        Arrays.stream(rectangles)
              .map(rectangle -> new Row(rectangle[0], rectangle[1], rectangle[2], rectangle[3]))
              .forEach(list::add);

        int listSize = list.size();

        list.subList(0, listSize - 1)
               .forEach(rectangle -> list.subList(++start, listSize)
                                            .stream()
                                            .filter(rectangle1 -> (((rectangle1.getX0() >= rectangle.getX0() && rectangle1.getX0() <= rectangle.getX1()) &&
                                                    (rectangle1.getY0() >= rectangle.getY0() && rectangle1.getY0() <= rectangle.getY1() ||
                                                            rectangle1.getY1() >= rectangle.getY0() && rectangle1.getY1() <= rectangle.getY1()))) ||
                                                    ((rectangle1.getX1() >= rectangle.getX0() && rectangle1.getX1() <= rectangle.getX1()) &&
                                                            (rectangle1.getY0() >= rectangle.getY0() && rectangle1.getY0() <= rectangle.getY1() ||
                                                                    rectangle1.getY1() >= rectangle.getY0() && rectangle1.getY1() <= rectangle.getY1())))
                                            .forEach(rectangle1 -> commonSpace += calculateCommonSpace(rectangle1, rectangle)));

        /*for (int i = 0; i < listSize - 1; i++)
            for (int j = i + 1; j < listSize; j++)
                if ((newList.get(j).getY0() >= newList.get(i).getY0() && newList.get(j).getY0() <= newList.get(i).getY1())
                        || (newList.get(j).getY1() >= newList.get(i).getY0() && newList.get(j).getY1() <= newList.get(i).getY1())) {
                    if ((newList.get(j).getX0() >= newList.get(i).getX0() && newList.get(j).getX0() <= newList.get(i).getX1())
                            || (newList.get(j).getX1() >= newList.get(i).getX0() && newList.get(j).getX1() <= newList.get(i).getX1()))
                        commonSpace += calculateCommonSpace(newList.get(j), newList.get(i));
                }*/

        return list.stream().mapToInt(rectangle -> (rectangle.getX1() - rectangle.getX0()) * (rectangle.getY1() - rectangle.getY0())).parallel().sum() - commonSpace;
    }

    private static List<Row> removeInnerRectangles(List<Row> list){
        List<Row> innerList = new ArrayList<>();

        list.forEach(outerRow -> list.stream()
                                     .filter(innerRow -> !innerRow.equals(outerRow))
                                     .filter(innerRow -> innerRow.getX0() >= outerRow.getX0() && innerRow.getY0() >= outerRow.getY0() && innerRow.getX1() <= outerRow.getX1() && innerRow.getY1() <= outerRow.getY1())
                                     .parallel()
                                     .forEach(innerList::add));

        if (innerList.size() == 0)
            return list;

        innerList.forEach(list::remove);

        return removeInnerRectangles(list);
    }

    private static int calculateCommonSpace(Row rowJ, Row rowI){
        int commonSpace;

        if (rowJ.getY0() >= rowI.getY0() && rowJ.getY1() <= rowI.getY1()) {
            if (rowJ.getX0() >= rowI.getX0() && rowJ.getX1() <= rowI.getX1())
                commonSpace = (rowJ.getX1() - rowJ.getX0()) * (rowJ.getY1() - rowJ.getY0());
            else if (rowJ.getX0() >= rowI.getX0())
                commonSpace = (rowI.getX1() - rowJ.getX0()) * (rowJ.getY1() - rowJ.getY0());
            else commonSpace = (rowJ.getX1() - rowI.getX0()) * (rowJ.getY1() - rowJ.getY0());
        }else if (rowJ.getY0() >= rowI.getY0()){
            if (rowJ.getX0() >= rowI.getX0() && rowJ.getX1() <= rowI.getX1())
                commonSpace = (rowJ.getX1() - rowJ.getX0()) * (rowI.getY1() - rowJ.getY0());
            else if (rowJ.getX0() >= rowI.getX0())
                commonSpace = (rowI.getX1() - rowJ.getX0()) * (rowI.getY1() - rowJ.getY0());
            else commonSpace = (rowJ.getX1() - rowI.getX0()) * (rowI.getY1() - rowJ.getY0());
        }else {
            if (rowJ.getX0() >= rowI.getX0() && rowJ.getX1() <= rowI.getX1())
                commonSpace = (rowJ.getX1() - rowJ.getX0()) * (rowJ.getY1() - rowI.getY0());
            else if (rowJ.getX0() >= rowI.getX0())
                commonSpace = (rowI.getX1() - rowJ.getX0()) * (rowJ.getY1() - rowI.getY0());
            else commonSpace = (rowJ.getX1() - rowI.getX0()) * (rowJ.getY1() - rowI.getY0());
        }

        return commonSpace == (rowJ.getX1() - rowJ.getX0());
    }
    public static void main(String[] args) {
        int[][] a = {{3,3,8,5}, {4,4,8,5}, {2,2,8,5}};

        System.out.println(Solution.calculateSpace(a));
    }
}
