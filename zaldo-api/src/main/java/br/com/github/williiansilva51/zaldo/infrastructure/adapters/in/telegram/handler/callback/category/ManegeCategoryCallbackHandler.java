package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.category;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
public class ManegeCategoryCallbackHandler implements TelegramCallbackHandler {
    @Override
    public String getActionName() {
        return BotAction.MANEGE_CATEGORY.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String text = """
                🗂️ <b>Gerenciar categorias</b>
                
                🚧 <i>Funcionalidade em desenvolvimento</i>
                
                Em breve você poderá:
                ➕ Criar categorias
                ✏️ Editar categorias
                ❌ Remover categorias
                🧩 Organizar seus registros
                
                Volte em breve! 🚀
                """;

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton(BotAction.CONFIG.getActionName())))
                        .build())
                .parseMode("HTML")
                .build();
    }
}
