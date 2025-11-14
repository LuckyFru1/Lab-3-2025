import functions.*;

public class Main {

    public static void main(String[] args) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException, IllegalArgumentException{
        System.out.println("Использование ArrayTabulatedFunction:");
        double[] data = {1, 3.7, 6.9, 8, 4, 2, 20, 15.5, 73, 100};
        FunctionPoint PointToAdd1 = new FunctionPoint(3.5, 5);
        FunctionPoint PointToAdd2 = new FunctionPoint(15, 6);
        FunctionPoint PointToAdd3 = new FunctionPoint(101, 20.5);

        final double EPSILON_DOUBLE = 1e-9;

        TabulatedFunction Function = new ArrayTabulatedFunction(1, 50, data);

        System.out.println("Исходная матрица:");
        for (int i = 0; i < Function.getPointsCount(); i++) {
            System.out.println("X[" + i + "] = " + Function.getPointX(i) + " and Y[" + i + "] = " + Function.getPointY(i));
        }

        System.out.println("\nУдаление точек с индексами 0, 4, 6");
        if (Function.getPointsCount() > 0) {
            Function.deletePoint(4);
            Function.deletePoint(2);
            Function.deletePoint(0);
        } 
        else {
            System.out.println("Нет точек для удаления");
        }

        System.out.println("\nМатрица после удаления точек:");
        for (int i = 0; i < Function.getPointsCount(); i++) {
            System.out.println("X[" + i + "] = " + Function.getPointX(i) + " and Y[" + i + "] = " + Function.getPointY(i));
        }

        System.out.println(data.length);
        System.out.println("\nДобавление точек ");
        try {
            Function.addPoint(PointToAdd1);
            Function.addPoint(PointToAdd2);
            Function.addPoint(PointToAdd3);
        } catch (InappropriateFunctionPointException mes) {
            System.out.println("\n Ошибка при добавлении точек: "+ mes.getMessage());
        }

        System.out.println("\nМатрица после добавления точек: ");
        for (int i = 0; i < Function.getPointsCount(); i++) {
            System.out.println("X[" + i + "] = " + Function.getPointX(i) + " and Y[" + i + "] = " + Function.getPointY(i));
        }
        System.out.println("\nРасчет значения Y с помощью линейной интерполяции");
        double ValueX = 50;
        double ValueY = Function.getFunctionValue(ValueX);
        System.out.println("Значение функции X = " + ValueX + " Y = " + ValueY);

        System.out.println("Использование LinkenListTabulatedFunction:");

        TabulatedFunction ListFunction = new LinkedListTabulatedFunction(1, 50, data);

        System.out.println("Исходная матрица:");
        for (int i = 0; i < ListFunction.getPointsCount(); i++) {
            System.out.println("X[" + i + "] = " + ListFunction.getPointX(i) + " and Y[" + i + "] = " + ListFunction.getPointY(i));
        }

        System.out.println("\nУдаление точек с индексами 0, 4, 6");
        if (ListFunction.getPointsCount() > 0) {
            ListFunction.deletePoint(4);
            ListFunction.deletePoint(2);
            ListFunction.deletePoint(0);
        } 
        else {
            System.out.println("Нет точек для удаления");
        }

        System.out.println("\nМатрица после удаления точек:");
        for (int i = 0; i < ListFunction.getPointsCount(); i++) {
            System.out.println("X[" + i + "] = " + ListFunction.getPointX(i) + " and Y[" + i + "] = " + ListFunction.getPointY(i));
        }

        System.out.println("\nДобавление точек ");
        try {
            ListFunction.addPoint(PointToAdd1);
            ListFunction.addPoint(PointToAdd2);
            ListFunction.addPoint(PointToAdd3);
        } catch (InappropriateFunctionPointException mes) {
            System.out.println("\n Ошибка при добавлении точек: "+ mes.getMessage());
        }
        System.out.println(ListFunction.getPointsCount());
        System.out.println("\nМатрица после добавления точек: ");
        for (int i = 0; i < ListFunction.getPointsCount(); i++) {
            System.out.println("X[" + i + "] = " + ListFunction.getPointX(i) + " and Y[" + i + "] = " + ListFunction.getPointY(i));
        }
        System.out.println("\nРасчет значения Y с помощью линейной интерполяции");
        double ListValueY = ListFunction.getFunctionValue(ValueX);
        System.out.println("Значение функции X = " + ValueX + " Y = " + ListValueY);
        
        //Проверка геттер и сеттер методов
        
        System.out.println("\n               Проверка get() и set() методов");
        
        int testIndex = 3;
        if (testIndex > Function.getPointsCount() || testIndex < 0) {
            System.out.println("Неправильный тестовый индекс");
        }
        else {
            double afterSetPointX = Function.getPointX(testIndex);
            try {
                Function.setPointX(testIndex, 16);
            }   catch (InappropriateFunctionPointException mes) {
                System.out.println("\nОшибка при измненении точки X :"+ mes.getMessage());
            }

            if (Math.abs(Function.getPointX(testIndex) - 16) > EPSILON_DOUBLE) {
                System.out.println("\nЕсть проблемы с get/set методами для точки X");
            }
            else {
                System.out.println("\nТочка X был успешно изменена: X = " + Function.getPointX(testIndex));
                try {
                    Function.setPointX(testIndex, afterSetPointX);
                } catch(InappropriateFunctionPointException mes){
                    System.out.println("\nОшибка при изменении точки X : "+ mes.getMessage());
                }
                System.out.println("\nВозращено начальное значение X: " + afterSetPointX);
            }
            
            double afterSetPointY = Function.getPointY(testIndex);
            Function.setPointY(testIndex, 6.8);
            if (Math.abs(Function.getPointY(testIndex) - 6.8) > EPSILON_DOUBLE) {
                System.out.println("Есть проблемы с get/set методами для точки Y");
            }
            else {
                System.out.println("\nТочка X был успешно изменена: Y = " + Function.getPointY(testIndex));
                Function.setPointY(testIndex, afterSetPointY);
                System.out.println("\nВозращено начальное значение Y: " + afterSetPointY);
            }

            FunctionPoint afterSetPoint = Function.getPoint(testIndex);
            FunctionPoint testPoint = new FunctionPoint(16, 10);
            try {
                Function.setPoint(testIndex, testPoint);
            } catch (InappropriateFunctionPointException mes) {
                System.out.println("\nОшибка при измненени точки :"+ mes.getMessage());
            }
            if(Math.abs(testPoint.getX() - Function.getPointX(testIndex)) > EPSILON_DOUBLE && Math.abs(testPoint.getY() - Function.getPointY(testIndex)) > EPSILON_DOUBLE){
                System.out.println("\nЕсть проблемы с get/set методами для точки");
            }
            else{
                System.out.println("\nТочка была успешно изменена: X = " + Function.getPointX(testIndex) + " Y = " + Function.getPointY(testIndex));
                try {
                    Function.setPoint(testIndex, afterSetPoint);
                } catch (InappropriateFunctionPointException mes) {
                    System.out.println("\nОшибка при измненении точки: "+ mes.getMessage());
                }
                System.out.println("\nВозращено начальное значение точки X = " + Function.getPointX(testIndex) + " Y = " + Function.getPointY(testIndex));
            }
        }
    }
}