package ru.tusur.gazpromedatomsk.job;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.tusur.gazpromedatomsk.model.Description;
import ru.tusur.gazpromedatomsk.model.Dish;
import ru.tusur.gazpromedatomsk.model.FoodValue;
import ru.tusur.gazpromedatomsk.model.Menu;
import ru.tusur.gazpromedatomsk.service.DishService;
import ru.tusur.gazpromedatomsk.service.MenuService;

@Component
@AllArgsConstructor
@Slf4j
public class EdaTomskParse {

  private final DishService dishService;
  private final MenuService menuService;

  private final int SIZE_CONTENT = 150;

  public void parseEda() {
    String url = "https://edatomsk.ru/";

    List<Dish> dishes = new ArrayList<>();

    try {
      Document doc = Jsoup.connect(url)
          .userAgent("Mozilla")
          .timeout(5000)
          .referrer("https://google.com")
          .get();

      Elements menu = doc.select("div.menulist__content");
      Elements labels = menu.select("div.menulist__content__label");
      Elements blocksMenu = menu.select("div.menulist__content__list");

      for (int i = 0; i < labels.size(); i++) {
        Elements itemsMenu = blocksMenu.get(i).select("div.menulistItem");
        for (Element item : itemsMenu) {
          if (dishService.existDish(item.id())) {
            dishes.add(dishService.getDish(item.id()));
          } else {

            String image = url + item.select("div.menulistItem__image").attr("style")
                .replace("background: url(/", "")
                .replace(")", "");
            String name = item.select("div.menulistItem__info").select("div.dish__name.disp")
                .html();
            String desc = item.select("div.menulistItem__info").select("div.dish__description")
                .first().ownText();
            String price = item.select("div.dishWPBlock.li_desktop").select("div.dish__price")
                .text();
            String weight = item.select("div.dishWPBlock.li_desktop").select("div.dish__weight")
                .text();
            Elements values = item.select("div.menulistItem__info").select("div.dish__description")
                .first().select("div.dish_bgu_line");

            List<Description> descriptions = new ArrayList<>();

            if (desc.length() > SIZE_CONTENT) {
              for (int j = 0; j < desc.length() / SIZE_CONTENT + 1; j++) {
                if (j == desc.length() / SIZE_CONTENT) {
                  descriptions
                      .add(dishService
                          .saveDescription(new Description(desc.substring(j * SIZE_CONTENT))));
                } else {
                  descriptions.add(dishService
                      .saveDescription(
                          new Description(
                              desc.substring(j * SIZE_CONTENT, (j + 1) * SIZE_CONTENT))));
                }
              }
            } else {
              descriptions.add(dishService.saveDescription(new Description(desc)));
            }

            Dish dish = new Dish();
            dish.setType(labels.get(i).text());
            dish.setId(item.id());
            dish.setPrice(Integer.parseInt(price));
            dish.setTitle(name);
            dish.setWeight(weight);
            dish.setDescription(descriptions);
            dish.setImage(image);

            if (values.size() > 0) {
              FoodValue foodValue = new FoodValue();
              foodValue.setProtein(values.get(0).ownText());
              foodValue.setCarb(values.get(1).ownText());
              foodValue.setFats(values.get(2).ownText());
              foodValue.setEnergyValue(values.get(3).ownText());

              dish.setFoodValue(dishService.saveFoodValue(foodValue));
            }
            dishes.add(dishService.saveDish(dish));
          }
        }
      }

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

      Menu menuToday = new Menu();
      menuToday.setDishes(dishes);
      menuToday.setCreatedAt(sdf.parse(sdf.format(new Date())));
      menuService.save(menuToday);

    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
  }

}
