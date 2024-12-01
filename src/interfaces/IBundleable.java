package interfaces;

import java.util.function.Consumer;

public interface IBundleable<T> {
    /* makes a copy of the object */
    public T getObjectCopy();

    /* sets the state of an object */
    public void setObjectState(T object);

    /* runs an operation on the object */
    public default void mutateInBundle(
        Consumer<T> mutateBundle,
        Consumer<Exception> handleError
    ) {
        try {
            // create a clone, then mutate it
            T bundledObject = getObjectCopy();
            mutateBundle.accept(bundledObject);
            setObjectState(bundledObject);

        } catch (Exception e) {
            // run function to handle errors
            handleError.accept(e);
        }
    }
}
