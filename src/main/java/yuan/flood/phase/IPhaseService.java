package yuan.flood.phase;

public interface IPhaseService<T,K>{
    T getService(K k);
}
