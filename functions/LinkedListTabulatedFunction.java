package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction{

    private class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev;
        private FunctionNode next;
        
        public FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) {
            this.point = point;
            this.prev = prev;
            this.next = next;
        }

        public FunctionPoint getPoint() {
            return point;
        }

        public void setPoint(FunctionPoint point) {
            this.point = point;
        }

        public FunctionNode getPrev() {
            return prev;
        }

        public void setPrev(FunctionNode prev) {
            this.prev = prev;
        }

        public FunctionNode getNext() {
            return next;
        }

        public void setNext(FunctionNode next) {
            this.next = next;
        }
    }
    
    private FunctionNode head;
    private int pointsCount;
    private final double EPSILON_DOUBLE = 1e-9;
    
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс" + index + " лежит вне границ массива" + pointsCount);
        }

        FunctionNode current;

        if (index < pointsCount / 2) {
            current = head.getNext();
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = head;
            for (int i = pointsCount; i > index; i--) {
                current = current.getPrev();
            }
        }

        return current;
    }
    
    private FunctionNode addNodeToTail(FunctionPoint point) {
        FunctionNode newNode = new FunctionNode(point, head.getPrev(), head);
        FunctionNode tail = head.getPrev();
        tail.setNext(newNode);
        head.setPrev(newNode);
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index, FunctionPoint point) {
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " лежит вне границ массива " + pointsCount);
        }
        if (index == pointsCount) {
            pointsCount++;
            return addNodeToTail(point);
        }
        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode prevNode = nextNode.getPrev();
        FunctionNode newNode = new FunctionNode(point, prevNode, nextNode);
        prevNode.setNext(newNode);
        nextNode.setPrev(newNode);
        pointsCount++;
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " лежит вне границ массива " + pointsCount);
        }
        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.getPrev();
        FunctionNode nextNode = nodeToDelete.getNext();
        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);
        pointsCount--;
        return nodeToDelete;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount){
        this.pointsCount = pointsCount;
        if (leftX >= rightX) {
            throw new IllegalStateException("В массиве не может быть только одна точка");
        }
        if (pointsCount < 3) {
            throw new IllegalStateException("В массиве не может быть только одна точка");
        }
        head = new FunctionNode(null, null, null);
        head.setNext(head);
        head.setPrev(head);
        double step = (rightX - leftX)/(pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            addNodeToTail(new FunctionPoint(leftX + step * i, 0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values){
        this.pointsCount = values.length;
        if (leftX >= rightX) {
            throw new IllegalStateException("В массиве не может быть только одна точка");
        }
        if (pointsCount < 3) {
            throw new IllegalStateException("В массиве не может быть только одна точка");
        }
        head = new FunctionNode(null, null, null);
        head.setNext(head);
        head.setPrev(head);
        double step = (rightX - leftX)/(pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            addNodeToTail(new FunctionPoint(leftX + step * i,  values[i]));
        }
    }

    @Override
    public double getLeftDomainBorder() {
        return head.getNext().getPoint().getX();
    }
    
    @Override
    public double getRightDomainBorder() {
        return head.getPrev().getPoint().getX();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x <  getLeftDomainBorder()|| x > getRightDomainBorder()) {
            return Double.NaN;
        }
        if (Math.abs(x - getLeftDomainBorder()) < EPSILON_DOUBLE) {
            return head.getNext().getPoint().getY();
        }
        if (Math.abs(x  - getRightDomainBorder()) < EPSILON_DOUBLE) {
            return head.getPrev().getPoint().getY();
        }
        else {
            int i;
            double value = 0;
            for (i = 0; x >= getNodeByIndex(i).getPoint().getX(); i++){
                FunctionNode Node = getNodeByIndex(i);
                if (Math.abs(x - Node.getPoint().getX()) < EPSILON_DOUBLE){
                   value = Node.getPoint().getY(); 
                }
                else{
                    value = Node.getPoint().getY() + (Node.getNext().getPoint().getY() - Node.getPoint().getY())*(x - Node.getPoint().getX())/(Node.getNext().getPoint().getX() - Node.getPoint().getX());
                }
            }
            return value;
        }
    }

    @Override
    public int getPointsCount() {
        return pointsCount;
    }
    
    @Override
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Получить точку с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        return new FunctionPoint(getNodeByIndex(index).getPoint().getX(), getNodeByIndex(index).getPoint().getY());
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Задать точку с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        FunctionNode currentNode = getNodeByIndex(index);
        if (index == 0) {
            if (point.getX() > getRightDomainBorder()) {
                throw new InappropriateFunctionPointException("Новая точка X (" + point.getX() + ") выходит за границы (больше правой границы).");
            }
        } else if (index == pointsCount - 1) {
            if (point.getX() < getLeftDomainBorder()) {
                throw new InappropriateFunctionPointException("Новая точка X (" + point.getX() + ") выходит за границы (меньше левой границы).");
            }
        }
        else {
            if (point.getX() < currentNode.getPrev().getPoint().getX() || point.getX() > currentNode.getNext().getPoint().getX()) {
                throw new InappropriateFunctionPointException("Новая точка X (" + point.getX() + ") выходит за границы соседних к ней точек.");
            }
        }
        currentNode.setPoint(point);
    }

    @Override
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Получить координату X с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        else{
            return getNodeByIndex(index).getPoint().getX();
        }
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Задать координату X с индексом " + index + " невозможно, так как размер массива" + this.pointsCount);
        }
        FunctionNode currentNode = getNodeByIndex(index);
        if (x <  currentNode.getPrev().getPoint().getX() || x > currentNode.getNext().getPoint().getX()) {
            throw new InappropriateFunctionPointException("Новая точка X (" + x + ") выходит за границы соседних к ней точек.");
        }
        currentNode.getPoint().setX(x);
    }

    @Override
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Получить координату Y с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        else{
            return  getNodeByIndex(index).getPoint().getY();
        }
    }

    @Override
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Задать координату Y с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        }
        else{
            FunctionNode currentNode = getNodeByIndex(index);
            currentNode.getPoint().setY(y);
        }
    }
    
    @Override
    public void deletePoint(int index){
		if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Удалить точку с индексом " + index + " невозможно, так как размер массива " + this.pointsCount);
        } 
        if (pointsCount < 3) {
            throw new IllegalStateException("В массиве меньше 3 точек");
        }
        deleteNodeByIndex(index); 
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point.getX() > getNodeByIndex(pointsCount - 1).getPoint().getX()){
            addNodeByIndex(pointsCount, point);
        }
        else{
            int i = 0;
            while (point.getX() > getNodeByIndex(i).getPoint().getX()) ++i;
            FunctionNode Node = getNodeByIndex(i);
            if (Math.abs(point.getX() - Node.getPoint().getX()) < EPSILON_DOUBLE) {
                throw new InappropriateFunctionPointException("Координата X добавляемой точки совпадает с уже сужествующим X = " +  Node.getPoint().getX());
            }
            else{
                addNodeByIndex(i, point);
            } 
        }
    }
}

