package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;


public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    String fileName;

    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String newLine;
            while ((newLine = reader.readLine()) != null) {
                List<String> data = Arrays.asList(newLine.split(";"));
                E entity = extractEntity(data);
                super.save(entity);
            }

        } catch (FileNotFoundException e) {
            File newfile = new File(fileName);
            try {
                newfile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);

    /**
     * Returns the string containing the entity.
     *
     * @param entity
     * @return
     */
    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity) {
        super.save(entity);
        writeToFile(entity);
        return entity;
    }

    protected void writeToFile(E entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {

            writer.write(createEntityAsString(entity));
            writer.newLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void writeAllToFile() {
        try {
            PrintWriter pw = new PrintWriter(fileName);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (E user : this.findAll()) {
                writer.write(createEntityAsString(user));
                writer.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public E delete(ID id) {
        E removed = this.findOne(id);
        super.delete(id);
        writeAllToFile();
        return removed;
    }

    @Override
    public E update(E entity) {
        super.update(entity);
        writeAllToFile();
        return entity;
    }
}
