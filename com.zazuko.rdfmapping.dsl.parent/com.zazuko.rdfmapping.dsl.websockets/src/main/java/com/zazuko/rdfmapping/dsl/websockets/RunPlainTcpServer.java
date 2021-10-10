package com.zazuko.rdfmapping.dsl.websockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.xtext.ide.server.LanguageServerImpl;
import org.eclipse.xtext.ide.server.ServerModule;

import com.google.common.base.Functions;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class RunPlainTcpServer {

	private static final Logger logger = Logger.getLogger(RunPlainTcpServer.class);
	
	public static void main(String[] args) throws InterruptedException, IOException {
		Injector injector = Guice.createInjector(new ServerModule());
		ServerSocket serverSocket = new ServerSocket(4389);

		logger.info("Language Server started.");

		try {
			while (true) {
				logger.info(
						"Waiting for client to connect to web socket on port " + serverSocket.getLocalPort() + "...");
				Socket socket = serverSocket.accept();
				try {
					logger.info("Connected.");
					LanguageServerImpl languageServer = injector.getInstance(LanguageServerImpl.class);
					Function<MessageConsumer, MessageConsumer> wrapper = Functions.identity();

					Launcher<LanguageClient> launcher = createSocketLauncher(languageServer, LanguageClient.class,
							Executors.newCachedThreadPool(), wrapper, socket.getInputStream(),
							socket.getOutputStream());
					languageServer.connect(launcher.getRemoteProxy());
					Future<?> future = launcher.startListening();
					while (!future.isDone()) {
						if (future.isDone()) {
							socket.close();
							languageServer.shutdown();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			serverSocket.close();
			logger.info("Language Server stopped.");
		}
	}

	private static Launcher<LanguageClient> createSocketLauncher(//
			Object localService, //
			Class<LanguageClient> remoteInterface, //
			ExecutorService executorService, //
			Function<MessageConsumer, MessageConsumer> wrapper, //
			InputStream inputStream, //
			OutputStream outputStream) throws IOException {
		return Launcher.createIoLauncher(localService, remoteInterface, inputStream, outputStream, executorService,
				wrapper);
	}

}
