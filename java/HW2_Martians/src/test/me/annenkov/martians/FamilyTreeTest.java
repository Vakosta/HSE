package me.annenkov.martians;

import me.annenkov.martians.exceptions.SeveralFamiliesException;
import me.annenkov.martians.models.ConservatorMartian;
import me.annenkov.martians.models.InnovatorMartian;
import me.annenkov.martians.models.Martian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FamilyTreeTest {
    private final List<InnovatorMartian<Integer>> martiansInteger = new ArrayList<>();
    private final List<InnovatorMartian<Double>> martiansDouble = new ArrayList<>();
    private final List<InnovatorMartian<String>> martiansString = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 6; i++) {
            martiansInteger.add(new InnovatorMartian<>(Integer.class, i));
            martiansDouble.add(new InnovatorMartian<>(Double.class, (double) i));
            martiansString.add(new InnovatorMartian<>(String.class, i + ""));
        }

        martiansInteger.get(0).setParent(martiansInteger.get(1));
        martiansInteger.get(1).setParent(martiansInteger.get(2));
        martiansInteger.get(2).setParent(martiansInteger.get(4));
        martiansInteger.get(3).setParent(martiansInteger.get(4));
        martiansInteger.get(4).setParent(martiansInteger.get(5));

        martiansDouble.get(0).setParent(martiansDouble.get(1));
        martiansDouble.get(1).setParent(martiansDouble.get(2));
        martiansDouble.get(2).setParent(martiansDouble.get(4));
        martiansDouble.get(3).setParent(martiansDouble.get(4));
        martiansDouble.get(4).setParent(martiansDouble.get(5));

        martiansString.get(0).setParent(martiansString.get(1));
        martiansString.get(1).setParent(martiansString.get(2));
        martiansString.get(2).setParent(martiansString.get(4));
        martiansString.get(3).setParent(martiansString.get(4));
        martiansString.get(4).setParent(martiansString.get(5));
    }

    @Test
    void getInstanceAndGetTextReport() {
        // Integer martians test.
        String report = new FamilyTree<>(new ArrayList<>(martiansInteger)).getTextReport();
        System.out.println("Фамильное дерево Integer:\n" + report);

        Class<?> type = FamilyTree.getCodeTypeByString(report);
        FamilyTree<?, ? extends Martian<?>> martiansNew;
        String reportNew;
        try {
            martiansNew = FamilyTree.getInstance(type, report);
            reportNew = martiansNew.getTextReport();
            assertEquals(report, reportNew);
        } catch (SeveralFamiliesException e) {
            e.printStackTrace();
            assertEquals(1, 0);
        }


        // Double martians test.
        report = new FamilyTree<>(new ArrayList<>(martiansDouble)).getTextReport();
        System.out.println("Фамильное дерево Double:\n" + report);

        type = FamilyTree.getCodeTypeByString(report);
        try {
            martiansNew = FamilyTree.getInstance(type, report);
            reportNew = martiansNew.getTextReport();
            assertEquals(report, reportNew);
        } catch (SeveralFamiliesException e) {
            e.printStackTrace();
            assertEquals(1, 0);
        }


        // String martians test.
        report = new FamilyTree<>(new ArrayList<>(martiansString)).getTextReport();
        System.out.println("Фамильное дерево String:\n" + report);

        type = FamilyTree.getCodeTypeByString(report);
        try {
            martiansNew = FamilyTree.getInstance(type, report);
            reportNew = martiansNew.getTextReport();
            assertEquals(report, reportNew);
        } catch (SeveralFamiliesException e) {
            e.printStackTrace();
            assertEquals(1, 0);
        }


        // Conservator martians test.
        ConservatorMartian<Integer> conservatorMartian = new ConservatorMartian<>(martiansInteger.get(0));
        report = new FamilyTree<>(conservatorMartian).getTextReport();
        System.out.println("Фамильное дерево консерваторов Integer:\n" + report);


        // New families test.
        for (int i = 0; i < 6; i++) {
            martiansInteger.add(new InnovatorMartian<>(Integer.class, i));
            martiansDouble.add(new InnovatorMartian<>(Double.class, (double) i));
            martiansString.add(new InnovatorMartian<>(String.class, i + ""));
        }
        report = new FamilyTree<>(new ArrayList<>(martiansString)).getTextReport();

        type = FamilyTree.getCodeTypeByString(report);
        try {
            FamilyTree.getInstance(type, report);
            assertEquals(1, 0);
        } catch (SeveralFamiliesException e) {
            // e.printStackTrace();
            assertEquals(1, 1);
        }
    }
}