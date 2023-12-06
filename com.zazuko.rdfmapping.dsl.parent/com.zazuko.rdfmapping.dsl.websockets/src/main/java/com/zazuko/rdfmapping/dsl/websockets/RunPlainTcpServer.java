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

import jakarta.inject.Inject;

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
	
	@Inject
	private Injector injector;
	
	public static void main(String[] args) throws InterruptedException, IOException {
		System.out.println("hi!");
		Injector injector = Guice.createInjector(new ServerModule());
		RunPlainTcpServer me = injector.getInstance(RunPlainTcpServer.class);
		me.work();
		System.out.println("bye!");
	}
	
	private void work() throws InterruptedException, IOException {
		ServerSocket serverSocket = new ServerSocket(4389);

		logger.info("Language Server started.");

		try {
			while (true) {
				logger.info(
						"Waiting for client to connect to web socket on port " + serverSocket.getLocalPort() + "...");
				Socket socket = serverSocket.accept();
				try {
					logger.info("Connected.");
					LanguageServerImpl languageServer = this.injector.getInstance(LanguageServerImpl.class);
					Function<MessageConsumer, MessageConsumer> wrapper = Functions.identity();

					Launcher<LanguageClient> launcher = createSocketLauncher(languageServer, LanguageClient.class,
							Executors.newCachedThreadPool(), wrapper, socket.getInputStream(),
							socket.getOutputStream());
					languageServer.connect(launcher.getRemoteProxy());
					Future<?> future = launcher.startListening();
					
					Thread housekeeper = new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								future.get();
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try {
									socket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
								languageServer.shutdown();
							}
						}
					});
					housekeeper.setDaemon(true);
					housekeeper.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			serverSocket.close();
			logger.info("Language Server stopped.");
		}
	}

	private Launcher<LanguageClient> createSocketLauncher(//
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
