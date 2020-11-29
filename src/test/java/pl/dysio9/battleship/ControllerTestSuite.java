package pl.dysio9.battleship;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ControllerTestSuite {

    @Test
    void testFindNonDiagonalNeighbors() {
        //given
        Controller controller = new Controller();
        controller.fillCellsListWithEmptyCells(true);

        //when
        List<Cell> neighbors = controller.findNonDiagonalNeighbors(new Cell(5,5,true));

        List<Cell> expectedNeighbors = new ArrayList<>();
        expectedNeighbors.add(new Cell(4,5,true));
        expectedNeighbors.add(new Cell(5,4,true));
        expectedNeighbors.add(new Cell(5,6,true));
        expectedNeighbors.add(new Cell(6,5,true));

        //then
        Assertions.assertEquals(expectedNeighbors,neighbors);
    }

//    public List<Cell> createPlaygroundPlayerList() {
//        List<Cell> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                Cell cell = new Cell(i, j, true, false);
//                list.add(cell);
//            }
//        }
//        return list;
//    }
}
