package by.protest.bot.handler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import by.protest.bot.command.Command;
import by.protest.bot.command.ParsedCommand;
import by.protest.bot.domain.entity.CarItem;
import by.protest.bot.domain.repository.ICarItemRepository;
import by.protest.bot.domain.repository.ISourceItemRepository;
import by.protest.bot.domain.repository.SpecificationBuilder;
import by.protest.bot.helper.Utils;
import by.protest.bot.instance.Bot;

@Component
public class RequestHandler {
	
	@Autowired
	private ICarItemRepository carItemRepository;

	@Autowired
	private ISourceItemRepository sourceRepository;

	@Autowired
	private Bot bot;

	private SpecificationBuilder specificationBuilder = new SpecificationBuilder();
	private static final String END_LINE = "\n";
	private static final String NO_INFO = "Няма інфармацыі па гэтаму запыту :(";
	private static final String LINE = "---" + END_LINE;
	private static final String START_MESSAGE = "Вас вітае партызанскі бот!";
	private static final String FOLLOWED_MESSAGE = "Увядзіце нумар аўтамабіля. "
			+ "Літары могуць быць лацінскія альбо кірылічныя. Даўжыня значымых сімвалаў не можа быць больш за 7. "
			+ "Пошук будзе ажыццяўляцца мінімум па 4 сімвалам.";
	private static final String LESS_THAN_NECESSARY_WARNING = "Вы ўвялі занадта мала значымых сімвалаў для пошуку!";
	private static final String TOO_MUCH_WARNING = "Вы ўвялі занадта шмат сімвалаў!";
	private static final String SEARCH_HAS_PERFORMED_BY = "Пошук ажыццяўляўся па сімвалам: ";
	private static final String NO_SIGNIFICANT_SIMBOLS = "У вашым запыце няма ні лічбаў ні літар, якія могуць прысутнічаць у нумары аўтамабіля.";
	private static final String NO_POSSIBLE_MATCHERS = "Праверце ці можна наогул з уведзеных вамі знакаў нейкім чынам скласці нумар альбо "
			+ "частку нумара аўтамабільнага рэгістрацыйнага знаку РБ";
	private static final String POSSIBLE_CHARS_NOTIFICATION = "Запыт можа складацца з лічбаў альбо літар A,B,C,E,H,I,K,M,O,P,T,X у любым рэгістры кірыліцай "
			+ "альбо лацініцай. Астатнія сімвалы будуць атфільтрованы.";
	private static final String CHECKING_LINE_1 = "POBEPKA";
	private static final String CHECKING_LINE_2 = "PABEPKA";
	private static final String CHECKING_ANSWER = "Калі гэта была праверка, то з ботам усё нармальна - ён працуе штатна.";

	public void operate(String chatId, ParsedCommand parsedCommand, Update update) {
		if (parsedCommand.getCommand() == Command.START) {
			sendMessage(chatId, START_MESSAGE.concat(END_LINE).concat(FOLLOWED_MESSAGE));
		} else
			operateRegistrationNumberRequest(chatId, parsedCommand.getText());
	}

	private void sendMessage(String chatId, String text) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText(text);
		bot.sendQueue.add(sendMessage);
	}

	private void sendMessage(String chatId, String text, List<String> sources) {
		synchronized (bot.sendQueue) {
			sendMessage(chatId, text);
			for (String source : sources) {
				SendMessage sendMessage = new SendMessage();
				sendMessage.setChatId(chatId);
				sendMessage.setText(source);
				bot.sendQueue.add(sendMessage);
			}
		}
	}

	private void operateRegistrationNumberRequest(String chatID, String registration) {
		System.out
				.println(LocalDateTime.now().toString() + "chatID: " + chatID + ": request received: " + registration);
		StringBuilder text = new StringBuilder();
		String requestedRegistrationChars = Utils.filter(registration.toUpperCase());
		List<CarItem> list = Collections.emptyList();
		if (CHECKING_LINE_1.equals(requestedRegistrationChars) || CHECKING_LINE_2.equals(requestedRegistrationChars)) {
			text.append(CHECKING_ANSWER);
			text.append(END_LINE);
		} else if (requestedRegistrationChars.length() == 0) {
			text.append(NO_SIGNIFICANT_SIMBOLS);
			text.append(END_LINE);
			text.append(POSSIBLE_CHARS_NOTIFICATION);
			text.append(END_LINE);
			text.append(FOLLOWED_MESSAGE);
		} else {
			text.append(SEARCH_HAS_PERFORMED_BY.concat(requestedRegistrationChars).concat(END_LINE));
			if (requestedRegistrationChars.length() < 4) {
				text.append(LESS_THAN_NECESSARY_WARNING);
				text.append(END_LINE);
				text.append(FOLLOWED_MESSAGE);
			} else if (requestedRegistrationChars.length() > 7) {
				text.append(TOO_MUCH_WARNING);
				text.append(END_LINE);
				text.append(FOLLOWED_MESSAGE);
			} else {
				list = this.carItemRepository
						.findAll(this.specificationBuilder.whereRegistrationLike(requestedRegistrationChars));
				if (list.isEmpty()) {
					if (Utils.matchesPossiblePattern(requestedRegistrationChars)) {
						text.append(NO_INFO.concat(END_LINE));
						text.append(LINE);
						text.append(END_LINE);
						text.append(FOLLOWED_MESSAGE);
					} else {
						text.append(NO_POSSIBLE_MATCHERS);
						text.append(END_LINE);
						text.append(POSSIBLE_CHARS_NOTIFICATION);
						text.append(END_LINE);
					}
				}
			}
		}
		sendMessage(chatID, text.toString());
		operateResultList(chatID, list);
	}

	private void operateResultList(String chatID, List<CarItem> list) {
		for (CarItem item : list.stream().sorted((c1, c2) -> c1.getRegistration().compareTo(c2.getRegistration()))
				.collect(Collectors.toList())) {
			StringBuilder text = new StringBuilder();
			text.append(LINE.concat(END_LINE));
			text.append(Utils.convertFromDbRepresentation(item.getRegistration()));
			text.append(END_LINE);
			if (item.getMark1() != null)
				text.append(item.getMark1()).append(END_LINE);
			if (item.getMark2() != null)
				text.append(item.getMark2()).append(END_LINE);
			text.append(item.getDescription()).append(END_LINE);
			if (item.getSource() != null) {
				text.append("крыніца інфармацыі: ");
				text.append(END_LINE);
				text.append(item.getSource());
				text.append(END_LINE);
			}
			text.append(LINE);
			text.append(END_LINE);
			if (item.getSourceAnchor() == null) {
				sendMessage(chatID, text.toString());
			} else
				sendMessage(chatID, text.toString(), sourceRepository.findByAnchor(item.getSourceAnchor()).stream()
						.map(im -> im.getSource()).collect(Collectors.toList()));
		}
		;
	}


}
