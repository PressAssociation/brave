package com.github.kristofa.brave;

import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;

/**
 * {@link ServerAndClientSpanState} implementation.
 * 
 * @author kristof
 */
class ServerAndClientSpanStateImpl implements ServerAndClientSpanState {

    private final static ThreadLocal<ServerSpan> currentServerSpan = new ThreadLocal<ServerSpan>() {

        @Override
        protected ServerSpanImpl initialValue() {
            return new ServerSpanImpl(null);
        }
    };
    private final static ThreadLocal<Span> currentClientSpan = new ThreadLocal<Span>();

    private final static ThreadLocal<String> currentClientServiceName = new ThreadLocal<String>();

    private Endpoint endPoint;

    public ServerAndClientSpanStateImpl() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServerSpan getCurrentServerSpan() {
        return currentServerSpan.get();
    }

    @Override
    public Endpoint getServerEndPoint() {
        return endPoint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentServerSpan(final ServerSpan span) {
        if (span == null) {
            currentServerSpan.remove();
        } else {
            currentServerSpan.set(span);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Endpoint getClientEndPoint() {
        final String serviceName = currentClientServiceName.get();
        if (serviceName == null) {
            return endPoint;
        } else {
            final Endpoint ep = new Endpoint();
            ep.setService_name(serviceName);
            return ep;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServerEndPoint(final Endpoint endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Span getCurrentClientSpan() {
        return currentClientSpan.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentClientSpan(final Span span) {

        currentClientSpan.set(span);
    }

    @Override
    public void setCurrentClientServiceName(final String serviceName) {
        currentClientServiceName.set(serviceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void incrementServerSpanThreadDuration(final long durationMs) {
        currentServerSpan.get().incThreadDuration(durationMs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getServerSpanThreadDuration() {
        return currentServerSpan.get().getThreadDuration();
    }

    @Override
    public Boolean sample() {
        return currentServerSpan.get().getSample();
    }

}
