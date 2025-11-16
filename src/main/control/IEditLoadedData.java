package src.main.control;

import src.main.entity.UserAccount;

public interface IEditLoadedData {
    public boolean create(UserAccount actor, String[] infoToMap);
    public Object  read(String id);
    public boolean update(String idToUpdate, String[] infoToMap);
    public boolean delete(String id);
}
