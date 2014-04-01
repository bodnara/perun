package cz.metacentrum.perun.webgui.tabs.memberstabs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.*;
import cz.metacentrum.perun.webgui.client.PerunWebSession;
import cz.metacentrum.perun.webgui.client.UiElements;
import cz.metacentrum.perun.webgui.client.resources.ButtonType;
import cz.metacentrum.perun.webgui.client.resources.PerunSearchEvent;
import cz.metacentrum.perun.webgui.client.resources.SmallIcons;
import cz.metacentrum.perun.webgui.client.resources.Utils;
import cz.metacentrum.perun.webgui.json.JsonCallbackEvents;
import cz.metacentrum.perun.webgui.json.JsonUtils;
import cz.metacentrum.perun.webgui.json.groupsManager.AddMember;
import cz.metacentrum.perun.webgui.json.groupsManager.GetAllGroups;
import cz.metacentrum.perun.webgui.model.Group;
import cz.metacentrum.perun.webgui.model.RichMember;
import cz.metacentrum.perun.webgui.tabs.TabItem;
import cz.metacentrum.perun.webgui.widgets.CustomButton;
import cz.metacentrum.perun.webgui.widgets.ExtendedSuggestBox;
import cz.metacentrum.perun.webgui.widgets.TabMenu;

import java.util.ArrayList;

/**
 * WIZARD "ADD SELECTED MEMBER TO GROUP(S)"
 * !! USE AS INNER TAB ONLY !!
 *
 * @author Pavel Zlamal <256627@mail.muni.cz>
 */
public class MemberAddToGroupTabItem implements TabItem {

    private RichMember member;
	private int memberId;
	private PerunWebSession session = PerunWebSession.getInstance();
	private SimplePanel contentWidget = new SimplePanel();
	private Label titleWidget = new Label("Loading member details");

	/**
	 * Constructor
	 *
     * @param member RichMember object, typically from table
     */
	public MemberAddToGroupTabItem(RichMember member){
		this.member = member;
		this.memberId = member.getId();
	}

	public boolean isPrepared(){
		return !(member == null);
	}

	public Widget draw() {

		this.titleWidget.setText(Utils.getStrippedStringWithEllipsis(member.getUser().getFullNameWithTitles().trim()) + ": add to group(s)");

        // main widget panel
		VerticalPanel vp = new VerticalPanel();
		vp.setSize("100%","100%");

        TabMenu menu = new TabMenu();
        vp.add(menu);
        vp.setCellHeight(menu, "30px");

        final GetAllGroups groups = new GetAllGroups(member.getVoId());

        final TabItem tab = this;
        final CustomButton addButton = TabMenu.getPredefinedButton(ButtonType.ADD, "Add member to selected group(s)");
        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                ArrayList<Group> list = groups.getTableSelectedList();
                if (UiElements.cantSaveEmptyListDialogBox(list)) {
                    // TODO - should have only one callback to core
                    for (int i=0; i<list.size(); i++) {
                        if (i == list.size()-1) {
                            // last
                            AddMember request = new AddMember(JsonCallbackEvents.closeTabDisableButtonEvents(addButton, tab));
                            request.addMemberToGroup(list.get(i), member);
                        } else {
                            AddMember request = new AddMember(JsonCallbackEvents.disableButtonEvents(addButton));
                            request.addMemberToGroup(list.get(i), member);
                        }
                    }
                }
            }
        });
        menu.addWidget(addButton);
        addButton.setEnabled(false);

        menu.addWidget(TabMenu.getPredefinedButton(ButtonType.CANCEL, "", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                session.getTabManager().closeTab(tab, false);
            }
        }));

        menu.addFilterWidget(new ExtendedSuggestBox(groups.getOracle()), new PerunSearchEvent() {
            @Override
            public void searchFor(String text) {
                groups.filterTable(text);
            }
        }, "Filter groups by name");

        CellTable<Group> table = groups.getTable();

        JsonUtils.addTableManagedButton(groups, table, addButton);

        table.addStyleName("perun-table");
        ScrollPanel sp = new ScrollPanel(table);
        sp.addStyleName("perun-tableScrollPanel");
        session.getUiElements().resizePerunTable(sp, 350, this);

        vp.add(sp);

		this.contentWidget.setWidget(vp);

		return getWidget();

	}

	public Widget getWidget() {
		return this.contentWidget;
	}

	public Widget getTitle() {
		return this.titleWidget;
	}

	public ImageResource getIcon() {
		return SmallIcons.INSTANCE.userGreenIcon();
	}

	@Override
	public int hashCode() {
		final int prime = 13;
		int result = 1;
		result = prime * result + memberId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberAddToGroupTabItem other = (MemberAddToGroupTabItem) obj;
		if (memberId != other.memberId)
			return false;
		return true;
	}

	public boolean multipleInstancesEnabled() {
		return false;
	}

	public void open() {}

	public boolean isAuthorized() {

		if (session.isVoAdmin(member.getVoId()) || session.isGroupAdmin()) {
			return true;
		} else {
			return false;
		}

	}

}