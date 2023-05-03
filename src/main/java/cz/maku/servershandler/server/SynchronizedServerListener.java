package cz.maku.servershandler.server;

@FunctionalInterface
public interface SynchronizedServerListener {

    void onUpdate(SynchronizedServer synchronizedServer);

}
