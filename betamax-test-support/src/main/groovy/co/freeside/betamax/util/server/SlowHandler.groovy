/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.freeside.betamax.util.server

import java.util.concurrent.CountDownLatch
import java.util.logging.Logger
import javax.servlet.http.*
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import static java.util.concurrent.TimeUnit.SECONDS

/**
 * A very dumb handler that will simply sit on any requests until it is told to shut down (i.e. the server is shutting
 * down). This is used for testing timeout conditions on clients.
 */
class SlowHandler extends AbstractHandler {

	private static final log = Logger.getLogger(SlowHandler.name)

	private final CountDownLatch stopLatch = new CountDownLatch(1)

	@Override
	void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
		log.fine "received $request.method request for $target..."
		stopLatch.await(30, SECONDS)
		log.fine 'request complete...'
	}

	@Override
	protected void doStop() {
		log.fine 'stopping handler...'
		stopLatch.countDown()
		super.doStop()
	}

}
