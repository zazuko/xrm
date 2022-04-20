package com.zazuko.rdfmapping.dsl.websockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.xtext.ide.server.LanguageServerImpl;
import org.eclipse.xtext.ide.server.ServerModule;

import com.google.common.base.Functions;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pmeade.websocket.io.WebSocketServerInputStream;
import com.pmeade.websocket.io.WebSocketServerOutputStream;
import com.pmeade.websocket.net.WebSocket;
import com.pmeade.websocket.net.WebSocketServerSocket;

public class RunWebSocketServerJava {

	public static void main(String[] args) throws InterruptedException, IOException {
		Injector injector = Guice.createInjector(new ServerModule());
		ServerSocket serverSocket = new ServerSocket(4389);
		WebSocketServerSocket webSocketServerSocket = new WebSocketServerSocket(serverSocket);

		System.out.println("Language Server started.");

		try {
			while (true) {
				System.out.println(
						"Waiting for client to connect to web socket on port " + serverSocket.getLocalPort() + "...");
				WebSocket webSocket = webSocketServerSocket.accept();
				try {
					System.out.println("Connected.");
					LanguageServerImpl languageServer = injector.getInstance(LanguageServerImpl.class);
					Function<MessageConsumer, MessageConsumer> wrapper = Functions.identity();

					Launcher<LanguageClient> launcher = createSocketLauncher(languageServer, LanguageClient.class,
							Executors.newCachedThreadPool(), wrapper, webSocket.getInputStream(),
							webSocket.getOutputStream());
					languageServer.connect(launcher.getRemoteProxy());
					Future<?> future = launcher.startListening();
					while (!future.isDone()) {
						if (future.isDone()) {
							webSocket.close();
							languageServer.shutdown();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			webSocketServerSocket.close();
			System.out.println("Language Server stopped.");
		}
	}

	private static Launcher<LanguageClient> createSocketLauncher(//
			Object localService, //
			Class<LanguageClient> remoteInterface, //
			ExecutorService executorService, //
			Function<MessageConsumer, MessageConsumer> wrapper, //
			WebSocketServerInputStream inputStream, //
			WebSocketServerOutputStream outputStream) throws IOException {
		return Launcher.createIoLauncher(localService, remoteInterface, inputStream, outputStream, executorService,
				wrapper);
	}

}
