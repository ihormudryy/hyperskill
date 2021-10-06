interface Movable {
    int getX();
    int getY();
    void setX(int newX);
    void setY(int newY);
}

interface Storable {
    int getInventoryLength();
    String getInventoryItem(int index);
    void setInventoryItem(int index, String item);
}

interface Command {
    void execute();
    void undo();
}

class CommandMove implements Command {
    Movable entity;
    int xMovement;
    int yMovement;

    @Override
    public void execute() {
        entity.setX(entity.getX() + xMovement);
        entity.setY(entity.getY() + yMovement);
    }

    @Override
    public void undo() {
        entity.setX(entity.getX() - xMovement);
        entity.setY(entity.getY() - yMovement);
    }
}

class CommandPutItem implements Command {
    Storable entity;
    String item;
    int lastIndex;

    @Override
    public void execute() {
        for (int i = 0; i < entity.getInventoryLength(); i++) {
            if (Objects.equals(entity.getInventoryItem(i), null)) {
                lastIndex = i;
                entity.setInventoryItem(i, item);
                break;
            }
        }
    }

    @Override
    public void undo() {
        entity.setInventoryItem(lastIndex, null);
    }
}
